package com.example.data.remote.service

import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseServiceToken
import retrofit2.http.Body
import retrofit2.http.POST

interface OnboardingService {

    @POST("/api/v1/auth/oauth")
    suspend fun postTokenToServiceToken(
        @Body request: RequestServiceTokenDto,
    ): BaseResponse<ResponseServiceToken>

}