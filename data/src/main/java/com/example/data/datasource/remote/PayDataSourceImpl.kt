package com.example.data.datasource.remote

import com.example.data.datasource.PayDataSource
import com.example.data.model.request.pay.RequestPayDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.pay.ResponsePayInAppDto
import com.example.data.model.response.pay.ResponsePaySubsDto
import com.example.data.model.response.pay.ResponsePurchaseInfoDto
import com.example.data.model.response.pay.ResponseSubsNeededDto
import com.example.data.model.response.pay.ResponseUserSubsInfoDto
import com.example.data.remote.service.PayService
import javax.inject.Inject

class PayDataSourceImpl @Inject constructor(
    private val payService: PayService,
) : PayDataSource {

    override suspend fun postToCheckSubsData(
        request: RequestPayDto,
    ): BaseResponse<ResponsePaySubsDto> {
        return payService.postToCheckSubs(request)
    }

    override suspend fun postToCheckInAppData(
        request: RequestPayDto,
    ): BaseResponse<ResponsePayInAppDto> {
        return payService.postToCheckInApp(request)
    }

    override suspend fun getSubsNeededData(): BaseResponse<ResponseSubsNeededDto> {
        return payService.getSubsNeeded()
    }

    override suspend fun getPurchaseInfoData(): BaseResponse<ResponsePurchaseInfoDto> {
        return payService.getPurchaseInfo()
    }

    override suspend fun getUserSubsInfoData(): BaseResponse<ResponseUserSubsInfoDto> {
        return payService.getUserSubsInfo()
    }
}
