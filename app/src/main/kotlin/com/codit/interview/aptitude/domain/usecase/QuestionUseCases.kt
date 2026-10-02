package com.codit.interview.aptitude.domain.usecase

import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.domain.model.QuestionNavigator
import com.codit.interview.aptitude.domain.model.QuestionSource
import com.codit.interview.aptitude.domain.model.SessionReport
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.repository.ProgressRepository
import com.codit.interview.aptitude.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/** Answers the current question and folds the result into the global statistics. */
class SubmitAnswerUseCase @Inject constructor(
    private val questions: QuestionRepository,
    private val progress: ProgressRepository,
) {
    /**
     * Persists the answer and updates the global statistics.
     *
     * Mock-test sessions pass [trackGlobally] `false` so they do not inflate the
     * practice percentages, which mirrors the legacy `FRAG_MOCK` special case.
     *
     * @return true when the chosen option was correct.
     */
    suspend operator fun invoke(
        source: QuestionSource,
        number: Int,
        optionIndex: Int,
        wasFirstAttempt: Boolean,
        trackGlobally: Boolean = true,
    ): Boolean {
        val question = questions.getQuestion(source, number) ?: return false
        val isCorrect = optionIndex == question.correctOptionIndex

        questions.recordAnswer(source, number, optionIndex)
        if (trackGlobally) {
            progress.registerAnswer(source.section, wasFirstAttempt, isCorrect)
        }
        return isCorrect
    }
}

/** Persists how long the user spent on a question. */
class RecordTimeTakenUseCase @Inject constructor(
    private val questions: QuestionRepository,
    private val progress: ProgressRepository,
) {
    suspend operator fun invoke(
        source: QuestionSource,
        number: Int,
        seconds: Int,
    ) {
        questions.recordTimeTaken(source, number, seconds)
        if (source !is QuestionSource.OfMockTest) {
            progress.registerTimeSpent(seconds)
        }
    }
}

/** Favourites the current question. */
class AddQuestionToFavouritesUseCase @Inject constructor(
    private val questions: QuestionRepository,
) {
    suspend operator fun invoke(source: QuestionSource, number: Int): Boolean =
        questions.addToFavourites(source, number)
}

/** Builds the end-of-session analytics report. */
class BuildSessionReportUseCase @Inject constructor() {
    operator fun invoke(
        title: String,
        navigator: QuestionNavigator,
        timePerQuestionSeconds: List<Int>,
    ): SessionReport = SessionReport(
        title = title,
        correct = navigator.correctCount,
        wrong = navigator.wrongCount,
        notAttempted = navigator.notAttemptedCount,
        total = navigator.total,
        timePerQuestionSeconds = timePerQuestionSeconds,
    )
}

/** Stores the note the user typed for a question. */
class SaveQuestionNoteUseCase @Inject constructor(
    private val questions: QuestionRepository,
) {
    suspend operator fun invoke(source: QuestionSource, number: Int, note: String) =
        questions.saveNote(source, number, note)
}

/** Favourited questions for the Favourites screen. */
class ObserveFavouriteQuestionsUseCase @Inject constructor(
    private val questions: QuestionRepository,
) {
    operator fun invoke(section: com.codit.interview.aptitude.domain.model.ContentSection): Flow<List<Question>> =
        questions.observeFavourites(section)
}

/** Per-topic completion percentage, used by the sub-category list. */
class ObserveTopicProgressUseCase @Inject constructor(
    private val progress: ProgressRepository,
) {
    operator fun invoke(topic: Topic): Flow<Int> = progress.observeTopicProgress(topic)
}

/** Records a topic's completion percentage when the user leaves a practice session. */
class UpdateTopicProgressUseCase @Inject constructor(
    private val progress: ProgressRepository,
) {
    suspend operator fun invoke(topic: Topic, percent: Int) =
        progress.setTopicProgress(topic, percent)
}
