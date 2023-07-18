package com.example.data.remote.service

import com.example.data.model.request.onboarding.RequestPostSignupDto
import com.example.data.model.request.onboarding.RequestServiceTokenDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseDepartmentDto
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

    @GET("api/v1/auth/school/school")
    suspend fun getSchoolSearchService(
        @Query("search") search: String,
        @Query("page") page: Long,
    ): BaseResponse<ResponseSchoolDto>

    @GET("api/v1/auth/school/department")
    suspend fun getDepartmentSearchService(
        @Query("school") school: String,
        @Query("search") search: String,
        @Query("page") page: Long,
    ): BaseResponse<ResponseDepartmentDto>

    @POST("/auth/signup")
    suspend fun postSignup(
        @Body requestPostSignupDto: RequestPostSignupDto,
    ): BaseResponse<ResponsePostSignupDto>
}
