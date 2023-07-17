package com.yello.di

import com.example.data.datasource.OnboardingDataSource
import com.example.data.datasource.RecommendDataSource
import com.example.data.datasource.YelloDataSource
import com.example.data.datasource.local.MockYelloDataSourceImpl
import com.example.data.datasource.remote.OnboardingDataSourceImpl
import com.example.data.datasource.remote.RecommendDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideYelloDataSource(yelloDataSource: MockYelloDataSourceImpl): YelloDataSource =
        yelloDataSource

    @Provides
    @Singleton
    fun provideOnboardingDataSource(onboardingDataSourceImpl: OnboardingDataSourceImpl): OnboardingDataSource =
        onboardingDataSourceImpl

    @Provides
    @Singleton
    fun provideRecommendDataSource(recommendDataSourceImpl: RecommendDataSourceImpl): RecommendDataSource =
        recommendDataSourceImpl
}