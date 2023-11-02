package com.example.data.repository

import com.example.data.datasource.PayDataSource
import com.example.data.model.request.pay.toRequestDto
import com.example.domain.entity.PayRequestModel
import com.example.domain.entity.PayInAppModel
import com.example.domain.entity.PaySubsModel
import com.example.domain.entity.PayInfoModel
import com.example.domain.entity.PaySubsNeededModel
import com.example.domain.repository.PayRepository
import javax.inject.Inject

class PayRepositoryImpl @Inject constructor(
    private val payDataSource: PayDataSource
) : PayRepository {

    override suspend fun postToCheckSubs(
        request: PayRequestModel
    ): Result<PaySubsModel?> {
        return runCatching {
            payDataSource.postToCheckSubsData(
                request.toRequestDto()
            ).data?.toResponsePaySubsModel()
        }
    }

    override suspend fun postToCheckInApp(
        request: PayRequestModel
    ): Result<PayInAppModel?> {
        return runCatching {
            payDataSource.postToCheckInAppData(
                request.toRequestDto()
            ).data?.toResponsePayInAppModel()
        }
    }

    override suspend fun getSubsNeeded(
    ): Result<PaySubsNeededModel?> {
        return runCatching {
            payDataSource.getSubsNeededData().data?.toResponseSubsNeededModel()
        }
    }

    override suspend fun getPurchaseInfo(
    ): Result<PayInfoModel?> {
        return runCatching {
            payDataSource.getPurchaseInfoData().data?.toResponsePurchaseInfoModel()
        }
    }

}