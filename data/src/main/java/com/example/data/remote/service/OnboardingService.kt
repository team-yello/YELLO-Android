package com.example.data.remote.service

import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.onboarding.ResponseServiceTokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface OnboardingService {

    @POST("/auth/oauth")
    suspend fun postTokenToServiceToken(
        @Body request: RequestServiceTokenDto,
    ): ResponseServiceTokenDto

}