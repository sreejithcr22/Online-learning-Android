package com.codit.interview.aptitude.data.repository

import com.codit.interview.aptitude.core.coroutines.DispatcherProvider
import com.codit.interview.aptitude.data.prefs.ProgressPreferences
import com.codit.interview.aptitude.data.prefs.SettingsPreferences
import com.codit.interview.aptitude.domain.model.AppSettings
import com.codit.interview.aptitude.domain.model.AppTheme
import com.codit.interview.aptitude.domain.repository.AppStateRepository
import com.codit.interview.aptitude.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/** App settings and theme, backed by the default `SharedPreferences` file. */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val preferences: SettingsPreferences,
    private val dispatchers: DispatcherProvider,
) : SettingsRepository {

    override fun observeSettings(): Flow<AppSettings> = preferences.observeSettings()

    override suspend fun updateSettings(settings: AppSettings) = withContext(dispatchers.io) {
        preferences.writeSettings(settings)
    }

    override fun observeTheme(): Flow<AppTheme> = preferences.observeTheme()

    override suspend fun setTheme(theme: AppTheme) = withContext(dispatchers.io) {
        preferences.writeTheme(theme)
    }
}

/** First-visit and review-prompt bookkeeping. */
@Singleton
class AppStateRepositoryImpl @Inject constructor(
    private val progressPreferences: ProgressPreferences,
    private val dispatchers: DispatcherProvider,
) : AppStateRepository {

    override fun isFirstVisit(): Boolean = progressPreferences.currentValues().visitCount == 0

    override suspend fun markVisited() = withContext(dispatchers.io) {
        progressPreferences.incrementVisitCount()
    }

    override fun hasRequestedReview(): Boolean = progressPreferences.hasRequestedReview()

    override suspend fun markReviewRequested() = withContext(dispatchers.io) {
        progressPreferences.markReviewRequested()
    }
}
