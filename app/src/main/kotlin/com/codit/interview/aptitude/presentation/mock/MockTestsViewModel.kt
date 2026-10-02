package com.codit.interview.aptitude.presentation.mock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.domain.model.MockTest
import com.codit.interview.aptitude.domain.usecase.ObserveMockDurationUseCase
import com.codit.interview.aptitude.domain.usecase.ObserveMockTestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class MockTestsUiState(
    val isLoading: Boolean = true,
    val tests: List<MockTest> = emptyList(),
    val durationSeconds: Int = 0,
    val completed: Int = 0,
    val averageScore: Int = 0,
)

/**
 * Mock-test list.
 *
 * Replaces `MockActivity` + `MockListFragment` + `MockAdapter`, which stored the mock
 * rows in a `static ArrayList` on the adapter and cast the host `Context` to a callback
 * interface.
 */
@HiltViewModel
class MockTestsViewModel @Inject constructor(
    observeMockTests: ObserveMockTestsUseCase,
    observeMockDuration: ObserveMockDurationUseCase,
) : ViewModel() {

    private val durationOverride = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<MockTestsUiState> = combine(
        observeMockTests(),
        observeMockDuration(),
        durationOverride,
    ) { tests, configuredDuration, override ->
        val completed = tests.filter { it.isFinished }
        MockTestsUiState(
            isLoading = false,
            tests = tests,
            durationSeconds = override ?: configuredDuration,
            completed = completed.size,
            averageScore = if (completed.isEmpty()) {
                0
            } else {
                completed.sumOf { it.score } / completed.size
            },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = MockTestsUiState(),
    )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
