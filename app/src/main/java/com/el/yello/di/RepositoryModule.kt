package com.el.yello.di

import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.OnboardingRepositoryImpl
import com.example.data.repository.ProfileRepositoryImpl
import com.example.data.repository.RecommendRepositoryImpl
import com.example.data.repository.VoteRepositoryImpl
import com.example.data.repository.YelloRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.OnboardingRepository
import com.example.domain.repository.ProfileRepository
import com.example.domain.repository.RecommendRepository
import com.example.domain.repository.VoteRepository
import com.example.domain.repository.YelloRepository
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
    fun provideYelloRepository(yelloRepositoryImpl: YelloRepositoryImpl): YelloRepository =
        yelloRepositoryImpl

    @Provides
    @Singleton
    fun provideVoteRepository(voteRepositoryImpl: VoteRepositoryImpl): VoteRepository =
        voteRepositoryImpl

    @Provides
    @Singleton
    fun provideOnboardingRepository(onboardingRepositoryImpl: OnboardingRepositoryImpl): OnboardingRepository =
        onboardingRepositoryImpl

    @Provides
    @Singleton
    fun provideProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository =
        profileRepositoryImpl

    @Provides
    @Singleton
    fun provideRecommendRepository(recommendRepositoryImpl: RecommendRepositoryImpl): RecommendRepository =
        recommendRepositoryImpl

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository =
        authRepositoryImpl

}