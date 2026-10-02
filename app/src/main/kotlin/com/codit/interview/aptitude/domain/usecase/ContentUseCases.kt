package com.codit.interview.aptitude.domain.usecase

import com.codit.interview.aptitude.domain.model.FormulaTopic
import com.codit.interview.aptitude.domain.model.InterviewTopic
import com.codit.interview.aptitude.domain.model.MockTest
import com.codit.interview.aptitude.domain.model.ProgressSnapshot
import com.codit.interview.aptitude.domain.model.Tip
import com.codit.interview.aptitude.domain.repository.MockTestRepository
import com.codit.interview.aptitude.domain.repository.ProgressRepository
import com.codit.interview.aptitude.domain.repository.QuestionRepository
import com.codit.interview.aptitude.domain.repository.TipRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Aggregate progress for the analytics dashboard. */
class ObserveProgressUseCase @Inject constructor(
    private val progress: ProgressRepository,
) {
    operator fun invoke(): Flow<ProgressSnapshot> = progress.observeProgress()
}

/** All tips of one interview or formula section. */
class ObserveTipsUseCase @Inject constructor(
    private val tips: TipRepository,
) {
    operator fun invoke(table: String): Flow<List<Tip>> = tips.observeTips(table)

    fun forInterviewTopic(topic: InterviewTopic): Flow<List<Tip>> = tips.observeTips(topic.table)

    fun forFormulaTopic(topic: FormulaTopic): Flow<List<Tip>> = tips.observeTips(topic.table)

    fun forFavourites(table: String): Flow<List<Tip>> = tips.observeTips(table)
}

/** Favourites a tip. */
class AddTipToFavouritesUseCase @Inject constructor(
    private val tips: TipRepository,
) {
    suspend operator fun invoke(table: String, tipNumber: Int, favouritesTable: String): Boolean =
        tips.addToFavourites(table, tipNumber, favouritesTable)
}

/** The formula for a given topic title, shown from the practice toolbar. */
class GetFormulaForTopicUseCase @Inject constructor(
    private val tips: TipRepository,
) {
    suspend operator fun invoke(formulaTable: String, topicTitle: String): Tip? =
        tips.getTipByTitle(formulaTable, topicTitle)
}

/** The list of ten mock tests with the user's scores. */
class ObserveMockTestsUseCase @Inject constructor(
    private val mockTests: MockTestRepository,
) {
    operator fun invoke(): Flow<List<MockTest>> = mockTests.observeMockTests()
}

/** Number of questions in a mock test, for the "25 questions" prompt. */
class MockTestQuestionCountUseCase @Inject constructor(
    private val mockTests: MockTestRepository,
) {
    suspend operator fun invoke(mockTest: MockTest): Int = mockTests.questionCount(mockTest)
}

/** Stores the score of a finished mock test. */
class RecordMockScoreUseCase @Inject constructor(
    private val mockTests: MockTestRepository,
) {
    suspend operator fun invoke(title: String, score: Int) = mockTests.recordScore(title, score)
}

/** Favourite counters shown on the Favourites screen tabs. */
class ObserveFavouriteCountsUseCase @Inject constructor(
    private val questions: QuestionRepository,
    private val tips: TipRepository,
) {
    data class Counts(val interview: Int, val formulas: Int, val aptitude: Int, val generalKnowledge: Int)

    operator fun invoke(): Flow<Counts> = kotlinx.coroutines.flow.combine(
        tips.observeTips(InterviewTopic.FAVOURITES_TABLE).map { it.size },
        tips.observeTips(FormulaTopic.FAVOURITES_TABLE).map { it.size },
        questions.observeFavourites(com.codit.interview.aptitude.domain.model.ContentSection.QUANTITATIVE)
            .map { it.size },
        questions.observeFavourites(com.codit.interview.aptitude.domain.model.ContentSection.GENERAL_KNOWLEDGE)
            .map { it.size },
    ) { interview, formulas, aptitude, gk -> Counts(interview, formulas, aptitude, gk) }
}
