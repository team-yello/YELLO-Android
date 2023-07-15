package com.yello.di

import com.example.data.repository.VoteRepositoryImpl
import com.example.data.repository.YelloRepositoryImpl
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
    fun provideYelloDataSource(yelloRepository: YelloRepositoryImpl): YelloRepository =
        yelloRepository

    @Provides
    @Singleton
    fun provideVoteDataSource(voteRepository: VoteRepositoryImpl): VoteRepository = voteRepository
}
