package com.yello.di

import com.example.data.datasource.OnboardingDataSource
import com.example.data.datasource.ProfileDataSource
import com.example.data.datasource.YelloDataSource
import com.example.data.datasource.local.MockYelloDataSourceImpl
import com.example.data.datasource.remote.OnboardingDataSourceImpl
import com.example.data.datasource.remote.ProfileDataSourceImpl
import com.example.data.datasource.remote.YelloDataSourceImpl
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
    fun provideYelloDataSource(yelloDataSource: YelloDataSourceImpl): YelloDataSource =
        yelloDataSource

    @Provides
    @Singleton
    fun provideOnboardingDataSource(onboardingDataSourceImpl: OnboardingDataSourceImpl): OnboardingDataSource =
        onboardingDataSourceImpl

    @Provides
    @Singleton
    fun provideProfileDataSource(profileDataSourceImpl: ProfileDataSourceImpl): ProfileDataSource =
        profileDataSourceImpl
}