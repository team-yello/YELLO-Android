package com.example.data.datasource

import com.example.data.model.request.onboarding.RequestPostSignupDto
import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.request.onboarding.RequestAddFriendDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseDepartmentDto
import com.example.data.model.response.onboarding.ResponseFriendListDto
import com.example.data.model.response.onboarding.ResponsePostSignupDto
import com.example.data.model.response.onboarding.ResponseSchoolDto
import com.example.data.model.response.onboarding.ResponseServiceTokenDto

interface OnboardingDataSource {
    suspend fun postTokenToServiceTokenData(
        requestServiceTokenDto: RequestServiceTokenDto,
    ): BaseResponse<ResponseServiceTokenDto>

    suspend fun getSchoolNameData(
        page: Int,
        search: String,
    ): BaseResponse<ResponseSchoolDto>

    suspend fun getDepartmentNameData(
        page: Int,
        school: String,
        search: String,
    ): BaseResponse<ResponseDepartmentDto>

    suspend fun getValidYelloId(
        yelloId: String,
    ): BaseResponse<Boolean>

    suspend fun postToGetFriendsListData(
        requestAddFriendDto: RequestAddFriendDto,
        page: Int,
    ): BaseResponse<ResponseFriendListDto>

    suspend fun postSignup(
        requestPostSignupDto: RequestPostSignupDto,
    ): BaseResponse<ResponsePostSignupDto>
}
