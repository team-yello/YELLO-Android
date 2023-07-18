package com.example.data.datasource.remote

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.RequestPostSignupDto
import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.request.onboarding.RequestSignFriendDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseDepartmentDto
import com.example.data.model.response.onboarding.ResponseFriendDto
import com.example.data.model.response.onboarding.ResponsePostSignupDto
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
        page: Int,
    ): BaseResponse<ResponseSchoolDto> {
        return onboardingService.getSchoolSearchService(search, page)
    }

    override suspend fun getDepartmentNameData(
        school: String,
        search: String,
        page: Int,
    ): BaseResponse<ResponseDepartmentDto> {
        return onboardingService.getDepartmentSearchService(school, search, page)
    }

    override suspend fun getIdValidData(yelloId: String): BaseResponse<Boolean> {
        return onboardingService.getIdValid(yelloId)
    }

    override suspend fun postFriendData(
        requestSignFriendDto: RequestSignFriendDto,
        page: Long,
    ): BaseResponse<ResponseFriendDto> {
        return onboardingService.postFriend(requestSignFriendDto, page)
    }

    override suspend fun postSignup(requestPostSignupDto: RequestPostSignupDto): BaseResponse<ResponsePostSignupDto> =
        onboardingService.postSignup(requestPostSignupDto)
}
