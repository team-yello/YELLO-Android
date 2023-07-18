package com.example.data.datasource.remote

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseDepartmentDto
import com.example.data.model.response.onboarding.ResponseSchoolDto
import com.example.data.model.response.onboarding.ResponseServiceTokenDto
import com.example.data.remote.service.OnboardingService
import javax.inject.Inject

class OnboardingDataSourceImpl @Inject constructor(
    private val onboardingService: OnboardingService,
) : OnboardingDataSource {

    override suspend fun postTokenToServiceTokenData(
        requestServiceTokenDto: RequestServiceTokenDto,
    ): BaseResponse<ResponseServiceTokenDto> {
        return onboardingService.postTokenToServiceToken(requestServiceTokenDto)
    }

    override suspend fun getSchoolNameData(
        search: String,
        page: Long,
    ): BaseResponse<ResponseSchoolDto> {
        return onboardingService.getSchoolSearchService(search, page)
    }

    override suspend fun getDepartmentNameData(
        school: String,
        search: String,
        page: Long,
    ): BaseResponse<ResponseDepartmentDto> {
        return onboardingService.getDepartmentSearchService(school, search, page)
    }
}
