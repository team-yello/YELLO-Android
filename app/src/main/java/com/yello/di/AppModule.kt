package com.yello.di

import android.content.Context
import com.example.data.util.FileParser
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
    fun provideFileParser(
        @ApplicationContext context: Context,
    ): FileParser = FileParser(context)
}
