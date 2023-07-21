package com.el.yello.di

import android.app.Application
import android.content.Context
import com.example.data.util.FileParser
import com.el.yello.presentation.util.ResolutionMetrics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplication(application: Application) = application

    @Provides
    @Singleton
    fun provideFileParser(
        @ApplicationContext context: Context,
    ): FileParser = FileParser(context)

    @Provides
    @Singleton
    fun provideResolutionMetrics(@ApplicationContext context: Application) =
        ResolutionMetrics(context)
}
