package com.yello.di

import com.example.data.repository.OnboardingRepositoryImpl
import com.example.data.repository.YelloRepositoryImpl
import com.example.domain.repository.OnboardingRepository
import com.example.domain.repository.YelloRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideYelloDataSource(yelloRepository: YelloRepositoryImpl): YelloRepository =
        yelloRepository

    @Binds
    @Singleton
    fun bindOnboardingRepository(onboardingRepositoryImpl: OnboardingRepositoryImpl): OnboardingRepository =
        onboardingRepositoryImpl
}