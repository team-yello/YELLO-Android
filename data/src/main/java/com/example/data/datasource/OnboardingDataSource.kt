package com.example.data.datasource

import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.request.onboarding.RequestSignFriendDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseDepartmentDto
import com.example.data.model.response.onboarding.ResponseFriendDto
import com.example.data.model.response.onboarding.ResponseSchoolDto
import com.example.data.model.response.onboarding.ResponseServiceTokenDto

interface OnboardingDataSource {
    suspend fun postTokenToServiceTokenData(
        requestServiceTokenDto: RequestServiceTokenDto,
    ): BaseResponse<ResponseServiceTokenDto>

    suspend fun getSchoolNameData(
        search: String,
        page: Long,
    ): BaseResponse<ResponseSchoolDto>

    suspend fun getDepartmentNameData(
        school: String,
        search: String,
        page: Long,
    ): BaseResponse<ResponseDepartmentDto>

    suspend fun getIdValidData(
        yelloId: String,
    ): BaseResponse<Boolean>

    suspend fun postFriendData(
        requestSignFriendDto: RequestSignFriendDto,
        page: Long,
    ): BaseResponse<ResponseFriendDto>
}
