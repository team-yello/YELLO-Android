package com.example.data.datasource.remote

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.RequestAddFriendDto
import com.example.data.model.request.onboarding.RequestPostSignupDto
import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseClassDto
import com.example.data.model.response.onboarding.ResponseDepartmentDto
import com.example.data.model.response.onboarding.ResponseFriendListDto
import com.example.data.model.response.onboarding.ResponseHighSchoolDto
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
        keyword: String,
        page: Int,
    ): BaseResponse<ResponseSchoolDto> {
        return onboardingService.getSchoolSearchService(keyword, page)
    }

    override suspend fun getHighSchoolNameData(
        keyword: String,
        page: Int,
    ): BaseResponse<ResponseHighSchoolDto> {
        return onboardingService.getHighSchoolSearchService(keyword, page)
    }

    override suspend fun getDepartmentNameData(
        page: Int,
        name: String,
        search: String,
    ): BaseResponse<ResponseDepartmentDto> {
        return onboardingService.getDepartmentSearchService(page, name, search)
    }

    override suspend fun getClassNameData(
        name: String,
        keyword: String,
    ): BaseResponse<ResponseClassDto> {
        return onboardingService.getClassSearchService(name, keyword)
    }

    override suspend fun getValidYelloId(yelloId: String): BaseResponse<Boolean> {
        return onboardingService.getValidYelloId(yelloId)
    }

    override suspend fun postToGetFriendsListData(
        requestAddFriendDto: RequestAddFriendDto,
        page: Int,
    ): BaseResponse<ResponseFriendListDto> {
        return onboardingService.postToGetFriendsList(requestAddFriendDto, page)
    }

    override suspend fun postSignup(requestPostSignupDto: RequestPostSignupDto): BaseResponse<ResponsePostSignupDto> =
        onboardingService.postSignup(requestPostSignupDto)
}
