package com.example.data.datasource.remote

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseServiceToken
import com.example.data.remote.service.OnboardingService
import javax.inject.Inject


class OnboardingDataSourceImpl @Inject constructor(
    private val onboardingService: OnboardingService
) : OnboardingDataSource {

    override suspend fun postTokenToServiceTokenData(
        requestServiceTokenDto: RequestServiceTokenDto
    ): BaseResponse<ResponseServiceToken> {
        return onboardingService.postTokenToServiceToken(requestServiceTokenDto)
    }
}