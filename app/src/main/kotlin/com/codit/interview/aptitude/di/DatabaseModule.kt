package com.codit.interview.aptitude.di

import android.content.Context
import android.content.SharedPreferences
import com.codit.interview.aptitude.data.local.InterviewDatabase
import com.codit.interview.aptitude.data.local.MasterDatabase
import com.codit.interview.aptitude.data.local.UserStateDatabase
import com.codit.interview.aptitude.data.prefs.ProgressPreferences
import com.codit.interview.aptitude.data.prefs.SettingsPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/** Qualifies the two `SharedPreferences` instances the app uses. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProgressPrefs

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultPrefs

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMasterDatabase(@ApplicationContext context: Context) = MasterDatabase(context)

    @Provides
    @Singleton
    fun provideInterviewDatabase(@ApplicationContext context: Context) = InterviewDatabase(context)

    @Provides
    @Singleton
    fun provideUserStateDatabase(@ApplicationContext context: Context) = UserStateDatabase(context)
}

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    @ProgressPrefs
    fun provideProgressPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("progress", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @DefaultPrefs
    fun provideDefaultPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(
        "com.codit.interview.aptitude_preferences",
        Context.MODE_PRIVATE,
    )

    @Provides
    @Singleton
    fun provideProgressPreferencesStore(
        @ProgressPrefs preferences: SharedPreferences,
    ) = ProgressPreferences(preferences)

    @Provides
    @Singleton
    fun provideSettingsPreferencesStore(
        @DefaultPrefs preferences: SharedPreferences,
    ) = SettingsPreferences(preferences)
}
