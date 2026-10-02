package com.codit.interview.aptitude.presentation.practice

import androidx.compose.runtime.Immutable
import com.codit.interview.aptitude.core.util.TimeFormat

/**
 * Visual state of a countdown.
 *
 * The legacy `QuestionFragBase` tracked this with four mutable `int` fields
 * (`sec`, `min`, `actualMin`, `actualSec`) and a `boolean updateTime` flag whose
 * meaning depended on the call site. One sealed type makes every state explicit.
 */
@Immutable
sealed interface TimerUiState {

    /** Timer has not been started for the current question. */
    data object Idle : TimerUiState

    /** Counting down. [remainingSeconds] goes negative once the budget is spent. */
    data class Running(
        val remainingSeconds: Int,
        val elapsedSeconds: Int,
    ) : TimerUiState {
        val isOvertime: Boolean get() = remainingSeconds < 0
        val display: String get() = TimeFormat.countdown(remainingSeconds)
    }

    /** The budget is spent; the user can still keep working. */
    data class Expired(val elapsedSeconds: Int) : TimerUiState

    /** Stopped, showing how long the question actually took. */
    data class Stopped(val elapsedSeconds: Int) : TimerUiState {
        val display: String get() = TimeFormat.clock(elapsedSeconds)
    }
}
