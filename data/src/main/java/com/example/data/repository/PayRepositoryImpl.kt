package com.example.data.repository

import com.example.data.datasource.PayDataSource
import com.example.data.model.request.pay.toRequestDto
import com.example.domain.entity.RequestPayModel
import com.example.domain.entity.ResponsePayInAppModel
import com.example.domain.entity.ResponsePaySubsModel
import com.example.domain.entity.ResponsePurchaseInfoModel
import com.example.domain.entity.ResponseSubsNeededModel
import com.example.domain.repository.PayRepository
import javax.inject.Inject

class PayRepositoryImpl @Inject constructor(
    private val payDataSource: PayDataSource
) : PayRepository {

    override suspend fun postToCheckSubs(
        request: RequestPayModel
    ): Result<ResponsePaySubsModel?> {
        return runCatching {
            payDataSource.postToCheckSubsData(
                request.toRequestDto()
            ).data?.toResponsePaySubsModel()
        }
    }

    override suspend fun postToCheckInApp(
        request: RequestPayModel
    ): Result<ResponsePayInAppModel?> {
        return runCatching {
            payDataSource.postToCheckInAppData(
                request.toRequestDto()
            ).data?.toResponsePayInAppModel()
        }
    }

    override suspend fun getSubsNeeded(
    ): Result<ResponseSubsNeededModel?> {
        return runCatching {
            payDataSource.getSubsNeededData().data?.toResponseSubsNeededModel()
        }
    }

    override suspend fun getPurchaseInfo(
    ): Result<ResponsePurchaseInfoModel?> {
        return runCatching {
            payDataSource.getPurchaseInfoData().data?.toResponsePurchaseInfoModel()
        }
    }

}