package com.codit.interview.aptitude.data.repository

import com.codit.interview.aptitude.core.coroutines.DispatcherProvider
import com.codit.interview.aptitude.core.util.SqliteText
import com.codit.interview.aptitude.data.local.UserStateDatabase
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.model.TopicTimer
import com.codit.interview.aptitude.domain.repository.TopicTimerRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/** Per-topic timer durations, persisted in `userstate.db`. */
@Singleton
class TopicTimerRepositoryImpl @Inject constructor(
    private val userState: UserStateDatabase,
    private val dispatchers: DispatcherProvider,
) : TopicTimerRepository {

    private val revision = MutableStateFlow(0L)

    override fun observeTimers(section: ContentSection): Flow<List<TopicTimer>> =
        revision.onStart { emit(revision.value) }
            .map { readTimers(section) }
            .flowOn(dispatchers.io)

    override suspend fun setTimer(topic: Topic, seconds: Int) = withContext(dispatchers.io) {
        userState.setTopicTimer(
            topic = topic.displayName,
            time = SqliteText.durationText(seconds),
            parent = topic.section.name,
        )
        revision.value = revision.value + 1
    }

    override suspend fun replaceDefaultTime(oldSeconds: Int, newSeconds: Int) =
        withContext(dispatchers.io) {
            userState.replaceTimeForAll(
                oldTime = SqliteText.durationText(oldSeconds),
                newTime = SqliteText.durationText(newSeconds),
            )
            revision.value = revision.value + 1
        }

    private fun readTimers(section: ContentSection): List<TopicTimer> {
        val stored = userState.readTopicTimers(section.name)
        return Topic.entries
            .filter { it.section == section }
            .map { topic ->
                TopicTimer(
                    topic = topic,
                    seconds = SqliteText.durationSeconds(stored[topic.displayName]) ?: 0,
                )
            }
    }
}
