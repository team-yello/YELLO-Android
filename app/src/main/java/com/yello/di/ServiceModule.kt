package com.yello.di

import com.example.data.remote.service.YelloService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideYelloService(retrofit: Retrofit): YelloService =
        retrofit.create(YelloService::class.java)
}