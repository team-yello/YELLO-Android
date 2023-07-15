package com.example.data.datasource

import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.onboarding.ResponseServiceTokenDto

interface OnboardingDataSource {
    suspend fun getServiceTokenData(
        requestServiceTokenDto: RequestServiceTokenDto
    ): ResponseServiceTokenDto
}