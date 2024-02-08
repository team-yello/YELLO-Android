package com.el.yello.di

import com.example.data.remote.service.AuthService
import com.example.data.remote.service.EventService
import com.example.data.remote.service.LookService
import com.example.data.remote.service.NoticeService
import com.example.data.remote.service.OnboardingService
import com.example.data.remote.service.PayService
import com.example.data.remote.service.ProfileService
import com.example.data.remote.service.RecommendService
import com.example.data.remote.service.SearchService
import com.example.data.remote.service.VoteService
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

    @Provides
    @Singleton
    fun provideVoteService(retrofit: Retrofit): VoteService =
        retrofit.create(VoteService::class.java)

    @Provides
    @Singleton
    fun provideOnboardingService(retrofit: Retrofit): OnboardingService =
        retrofit.create(OnboardingService::class.java)

    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @Provides
    @Singleton
    fun provideRecommendService(retrofit: Retrofit): RecommendService =
        retrofit.create(RecommendService::class.java)

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)

    @Provides
    @Singleton
    fun provideLookService(retrofit: Retrofit): LookService =
        retrofit.create(LookService::class.java)

    @Provides
    @Singleton
    fun providePayService(retrofit: Retrofit): PayService =
        retrofit.create(PayService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideNoticeService(retrofit: Retrofit): NoticeService =
        retrofit.create(NoticeService::class.java)

    @Provides
    @Singleton
    fun provideEventService(retrofit: Retrofit): EventService =
        retrofit.create(EventService::class.java)
}
