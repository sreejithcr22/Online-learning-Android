package com.codit.interview.aptitude.core.coroutines

import javax.inject.Qualifier

/**
 * A [kotlinx.coroutines.CoroutineScope] that lives as long as the process.
 *
 * Needed for fire-and-forget writes issued from a `ViewModel.onCleared()`, where
 * `viewModelScope` has already been cancelled.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope
