package com.codit.interview.aptitude.di

import com.codit.interview.aptitude.core.coroutines.ApplicationScope
import com.codit.interview.aptitude.core.coroutines.DefaultDispatcherProvider
import com.codit.interview.aptitude.core.coroutines.DispatcherProvider
import com.codit.interview.aptitude.data.repository.AppStateRepositoryImpl
import com.codit.interview.aptitude.data.repository.MockTestRepositoryImpl
import com.codit.interview.aptitude.data.repository.ProgressRepositoryImpl
import com.codit.interview.aptitude.data.repository.QuestionRepositoryImpl
import com.codit.interview.aptitude.data.repository.SettingsRepositoryImpl
import com.codit.interview.aptitude.data.repository.TipRepositoryImpl
import com.codit.interview.aptitude.data.repository.TopicTimerRepositoryImpl
import com.codit.interview.aptitude.domain.repository.AppStateRepository
import com.codit.interview.aptitude.domain.repository.MockTestRepository
import com.codit.interview.aptitude.domain.repository.ProgressRepository
import com.codit.interview.aptitude.domain.repository.QuestionRepository
import com.codit.interview.aptitude.domain.repository.SettingsRepository
import com.codit.interview.aptitude.domain.repository.TipRepository
import com.codit.interview.aptitude.domain.repository.TopicTimerRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Process-lifetime scope for writes that must outlive the `ViewModel` that started
 * them, such as persisting session progress when the user leaves a practice session.
 */
@Module
@InstallIn(SingletonComponent::class)
object ApplicationScopeModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
}

/**
 * Binds every domain-layer interface to its data-layer implementation.
 *
 * This is the only place that knows both sides exist, which is what keeps the
 * dependency arrow pointing inwards: `presentation -> domain <- data`.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuestionRepository(impl: QuestionRepositoryImpl): QuestionRepository

    @Binds
    @Singleton
    abstract fun bindMockTestRepository(impl: MockTestRepositoryImpl): MockTestRepository

    @Binds
    @Singleton
    abstract fun bindTipRepository(impl: TipRepositoryImpl): TipRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindTopicTimerRepository(impl: TopicTimerRepositoryImpl): TopicTimerRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindAppStateRepository(impl: AppStateRepositoryImpl): AppStateRepository

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(impl: DefaultDispatcherProvider): DispatcherProvider
}
