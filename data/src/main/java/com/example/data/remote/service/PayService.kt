package com.example.data.remote.service

import com.example.data.model.request.pay.RequestPayDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.pay.ResponsePayInAppDto
import com.example.data.model.response.pay.ResponsePaySubsDto
import com.example.data.model.response.pay.ResponsePurchaseInfoDto
import com.example.data.model.response.pay.ResponseUserSubsInfoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PayService {

    @POST("/api/v1/purchase/google/verify/subscribe")
    suspend fun postToCheckSubs(
        @Body request: RequestPayDto,
    ): BaseResponse<ResponsePaySubsDto>

    @POST("/api/v1/purchase/google/verify/ticket")
    suspend fun postToCheckInApp(
        @Body request: RequestPayDto,
    ): BaseResponse<ResponsePayInAppDto>

    @GET("/api/v1/purchase")
    suspend fun getPurchaseInfo(): BaseResponse<ResponsePurchaseInfoDto>

    @GET("/api/v1/user/subscribe")
    suspend fun getUserSubsInfo(): BaseResponse<ResponseUserSubsInfoDto>
}
