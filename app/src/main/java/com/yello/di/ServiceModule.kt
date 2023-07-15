package com.yello.di

import com.example.data.remote.service.VoteService
import com.example.data.remote.service.YelloService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideYelloService(retrofit: Retrofit): YelloService =
        retrofit.create(YelloService::class.java)

    @Provides
    @Singleton
    fun provideVoteService(retrofit: Retrofit): VoteService =
        retrofit.create(VoteService::class.java)
}
