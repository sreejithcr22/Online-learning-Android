package com.codit.interview.aptitude.data.repository

import com.codit.interview.aptitude.core.coroutines.DispatcherProvider
import com.codit.interview.aptitude.data.local.MasterDatabase
import com.codit.interview.aptitude.data.local.UserStateDatabase
import com.codit.interview.aptitude.domain.model.MockTest
import com.codit.interview.aptitude.domain.repository.MockTestRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/** Mock-test scores from `userstate.db`, question counts from `master.db`. */
@Singleton
class MockTestRepositoryImpl @Inject constructor(
    private val userState: UserStateDatabase,
    private val master: MasterDatabase,
    private val dispatchers: DispatcherProvider,
) : MockTestRepository {

    private val revision = MutableStateFlow(0L)

    override fun observeMockTests(): Flow<List<MockTest>> =
        revision.onStart { emit(revision.value) }
            .map { readMockTests() }
            .flowOn(dispatchers.io)

    override suspend fun recordScore(title: String, score: Int) = withContext(dispatchers.io) {
        userState.recordScore(title, score)
        revision.value = revision.value + 1
    }

    override suspend fun questionCount(mockTest: MockTest): Int = withContext(dispatchers.io) {
        master.lastQuestionNumber(mockTest.table)
    }

    private fun readMockTests(): List<MockTest> {
        return userState.readMockTests().mapIndexed { index, row ->
            val number = index + 1
            MockTest(
                index = number,
                title = row.title,
                score = row.score,
                isFinished = row.isFinished,
                isLocked = row.isLocked,
                questionCount = master.lastQuestionNumber("mock$number"),
            )
        }
    }
}
