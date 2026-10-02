package com.codit.interview.aptitude.core.coroutines

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

/**
 * Emits immediately and then once every [periodMillis] until the collecting
 * coroutine is cancelled.
 *
 * This is the replacement for the several hand-rolled `Timer` + `TimerTask` +
 * `Handler.post` combinations the legacy activities used to drive their countdowns.
 * Because it is a cold [Flow], the ticker is scoped to the collector and stops by
 * itself when the ViewModel is cleared.
 */
fun tickerFlow(periodMillis: Long = 1_000L): Flow<Unit> = flow {
    while (currentCoroutineContext().isActive) {
        emit(Unit)
        delay(periodMillis)
    }
}
