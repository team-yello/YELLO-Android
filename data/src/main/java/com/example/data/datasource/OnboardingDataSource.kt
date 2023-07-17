package com.example.data.datasource

import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseSchoolDto
import com.example.data.model.response.onboarding.ResponseServiceTokenDto

interface OnboardingDataSource {
    suspend fun postTokenToServiceTokenData(
        requestServiceTokenDto: RequestServiceTokenDto,
    ): ResponseServiceTokenDto

    suspend fun getSchoolNameData(
        search: String,
        page: Long,
    ): BaseResponse<ResponseSchoolDto>
}
