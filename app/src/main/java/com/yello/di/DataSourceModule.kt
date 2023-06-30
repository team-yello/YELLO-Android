package com.yello.di

import com.example.data.datasource.YelloDataSource
import com.example.data.datasource.local.MockYelloDataSourceImpl
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
}