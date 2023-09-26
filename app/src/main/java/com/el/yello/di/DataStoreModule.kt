package com.el.yello.di

import android.content.Context
import android.content.SharedPreferences
import com.example.data.local.qualifier.App
import com.example.data.local.qualifier.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val APP_PREFERENCES_NAME = "APP_DATA"
    private const val USER_PREFERENCES_NAME = "USER_DATA"

    @Provides
    @Singleton
    @User
    fun provideUserPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @App
    fun provideAppPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE)
}