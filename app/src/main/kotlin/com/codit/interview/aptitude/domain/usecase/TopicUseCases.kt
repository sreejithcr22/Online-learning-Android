package com.codit.interview.aptitude.domain.usecase

import com.codit.interview.aptitude.domain.model.AppSettings
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.repository.SettingsRepository
import com.codit.interview.aptitude.domain.repository.TopicTimerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/** A practice topic paired with the duration configured for it. */
data class TopicWithTimer(val topic: Topic, val seconds: Int)

private fun topicsIn(section: ContentSection): List<Topic> = when (section) {
    ContentSection.QUANTITATIVE -> Topic.quantitative
    ContentSection.LOGICAL -> Topic.logical
    ContentSection.VERBAL -> Topic.verbal
    ContentSection.GENERAL_KNOWLEDGE -> Topic.generalKnowledge
}

/** Topics of a section paired with their stored timer durations. */
class GetTopicsWithTimersUseCase @Inject constructor(
    private val topicTimers: TopicTimerRepository,
) {
    operator fun invoke(section: ContentSection): Flow<List<TopicWithTimer>> =
        topicTimers.observeTimers(section).map { timers ->
            val byTopic = timers.associateBy { it.topic }
            topicsIn(section).map { topic ->
                TopicWithTimer(topic, byTopic[topic]?.seconds ?: 0)
            }
        }
}

/**
 * Topics of a section with the *effective* duration: a per-topic override when one
 * exists, otherwise the global default from settings.
 */
class ResolveTopicTimersUseCase @Inject constructor(
    private val getTopicsWithTimers: GetTopicsWithTimersUseCase,
    private val settingsRepository: SettingsRepository,
) {
    data class Resolved(val topic: Topic, val seconds: Int)

    operator fun invoke(section: ContentSection): Flow<List<Resolved>> = combine(
        getTopicsWithTimers(section),
        settingsRepository.observeSettings(),
    ) { timers, settings ->
        timers.map { timer ->
            val resolved = if (timer.seconds > 0) timer.seconds else settings.defaultQuestionSeconds
            Resolved(timer.topic, resolved)
        }
    }
}

/** Updates the timer of a single topic. */
class SetTopicTimerUseCase @Inject constructor(
    private val topicTimers: TopicTimerRepository,
) {
    suspend operator fun invoke(topic: Topic, seconds: Int) = topicTimers.setTimer(topic, seconds)
}

/**
 * Changes the global default timer.
 *
 * Every topic that was still on the previous default is migrated too, so the change
 * behaves the way users expect from "Default time" in settings.
 */
class ChangeDefaultTimerUseCase @Inject constructor(
    private val topicTimers: TopicTimerRepository,
    private val settings: SettingsRepository,
) {
    suspend operator fun invoke(oldSeconds: Int, newSeconds: Int) {
        topicTimers.replaceDefaultTime(oldSeconds, newSeconds)
        val current: AppSettings = settings.observeSettings().first()
        settings.updateSettings(current.copy(defaultQuestionSeconds = newSeconds))
    }
}

/** Reads and writes the app settings shown on the settings screen. */
class ObserveSettingsUseCase @Inject constructor(
    private val settings: SettingsRepository,
) {
    operator fun invoke(): Flow<AppSettings> = settings.observeSettings()
}

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(settings: AppSettings) = repository.updateSettings(settings)
}

/** Reads the mock-test duration configured in settings. */
class ObserveMockDurationUseCase @Inject constructor(
    private val settings: SettingsRepository,
) {
    operator fun invoke(): Flow<Int> = settings.observeSettings().map { it.mockTestSeconds }
}
