package com.example.data.remote.service

import com.example.data.model.request.onboarding.RequestAddFriendDto
import com.example.data.model.request.onboarding.RequestPostSignupDto
import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseDepartmentDto
import com.example.data.model.response.onboarding.ResponseFriendListDto
import com.example.data.model.response.onboarding.ResponsePostSignupDto
import com.example.data.model.response.onboarding.ResponseSchoolDto
import com.example.data.model.response.onboarding.ResponseServiceTokenDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OnboardingService {
    @POST("api/v1/auth/oauth")
    suspend fun postTokenToServiceToken(
        @Body request: RequestServiceTokenDto,
    ): BaseResponse<ResponseServiceTokenDto>

    @GET("api/v1/auth/school")
    suspend fun getSchoolSearchService(
        @Query("page") page: Int,
        @Query("keyword") keyword: String,
    ): BaseResponse<ResponseSchoolDto>

    @GET("api/v1/auth/school/department")
    suspend fun getDepartmentSearchService(
        @Query("page") page: Int,
        @Query("school") school: String,
        @Query("keyword") keyword: String,
    ): BaseResponse<ResponseDepartmentDto>

    @GET("api/v1/auth/valid")
    suspend fun getValidYelloId(
        @Query("yelloId") yelloId: String,
    ): BaseResponse<Boolean>

    @POST("api/v1/auth/friend")
    suspend fun postToGetFriendsList(
        @Body requestAddFriendDto: RequestAddFriendDto,
        @Query("page") page: Int,
    ): BaseResponse<ResponseFriendListDto>

    @POST("api/v1/auth/signup")
    suspend fun postSignup(
        @Body requestPostSignupDto: RequestPostSignupDto,
    ): BaseResponse<ResponsePostSignupDto>
}
