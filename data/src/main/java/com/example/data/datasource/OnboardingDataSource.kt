package com.example.data.datasource

import com.example.data.model.request.onboarding.RequestPostSignupDto
import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.request.onboarding.RequestOnboardingListDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseDepartmentDto
import com.example.data.model.response.onboarding.ResponseFriendDto
import com.example.data.model.response.onboarding.ResponsePostSignupDto
import com.example.data.model.response.onboarding.ResponseSchoolDto
import com.example.data.model.response.onboarding.ResponseServiceTokenDto

interface OnboardingDataSource {
    suspend fun postTokenToServiceTokenData(
        requestServiceTokenDto: RequestServiceTokenDto,
    ): BaseResponse<ResponseServiceTokenDto>

    suspend fun getSchoolNameData(
        search: String,
        page: Int,
    ): BaseResponse<ResponseSchoolDto>

    suspend fun getDepartmentNameData(
        school: String,
        search: String,
        page: Int,
    ): BaseResponse<ResponseDepartmentDto>

    suspend fun getValidYelloId(
        yelloId: String,
    ): BaseResponse<Boolean>

    suspend fun postFriendData(
        requestSignFriendDto: RequestOnboardingListDto,
        page: Long,
    ): BaseResponse<ResponseFriendDto>

    suspend fun postSignup(
        requestPostSignupDto: RequestPostSignupDto,
    ): BaseResponse<ResponsePostSignupDto>
}
