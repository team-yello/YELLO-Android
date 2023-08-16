package com.example.data.datasource

import com.example.data.model.request.pay.RequestPayDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.pay.ResponsePayInAppDto
import com.example.data.model.response.pay.ResponsePaySubsDto

interface PayDataSource {

    suspend fun postToCheckSubsData(
        request: RequestPayDto
    ): BaseResponse<ResponsePaySubsDto>

    suspend fun postToCheckInAppData(
        request: RequestPayDto
    ): BaseResponse<ResponsePayInAppDto>

}