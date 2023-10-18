package com.el.yello.di

import com.example.data.datasource.AuthDataSource
import com.example.data.datasource.OnboardingDataSource
import com.example.data.datasource.PayDataSource
import com.example.data.datasource.ProfileDataSource
import com.example.data.datasource.RecommendDataSource
import com.example.data.datasource.SearchDataSource
import com.example.data.datasource.VoteDataSource
import com.example.data.datasource.YelloDataSource
import com.example.data.datasource.remote.AuthDataSourceImpl
import com.example.data.datasource.remote.OnboardingDataSourceImpl
import com.example.data.datasource.remote.PayDataSourceImpl
import com.example.data.datasource.remote.ProfileDataSourceImpl
import com.example.data.datasource.remote.RecommendDataSourceImpl
import com.example.data.datasource.remote.SearchDataSourceImpl
import com.example.data.datasource.remote.VoteDataSourceImpl
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
    fun provideYelloDataSource(yelloDataSourceImpl: YelloDataSourceImpl): YelloDataSource =
        yelloDataSourceImpl

    @Provides
    @Singleton
    fun provideVoteDataSource(voteDataSourceImpl: VoteDataSourceImpl): VoteDataSource =
        voteDataSourceImpl

    @Provides
    @Singleton
    fun provideOnboardingDataSource(onboardingDataSourceImpl: OnboardingDataSourceImpl): OnboardingDataSource =
        onboardingDataSourceImpl

    @Provides
    @Singleton
    fun provideProfileDataSource(profileDataSourceImpl: ProfileDataSourceImpl): ProfileDataSource =
        profileDataSourceImpl

    @Provides
    @Singleton
    fun provideRecommendDataSource(recommendDataSourceImpl: RecommendDataSourceImpl): RecommendDataSource =
        recommendDataSourceImpl

    @Provides
    @Singleton
    fun provideSearchDataSource(searchDataSourceImpl: SearchDataSourceImpl): SearchDataSource =
        searchDataSourceImpl

    @Provides
    @Singleton
    fun providePayDataSource(payDataSourceImpl: PayDataSourceImpl): PayDataSource =
        payDataSourceImpl

    @Provides
    @Singleton
    fun provideAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource =
        authDataSourceImpl
}
