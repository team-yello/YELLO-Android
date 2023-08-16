package com.example.data.repository

import com.example.data.datasource.PayDataSource
import com.example.data.model.request.pay.toRequestDto
import com.example.domain.entity.RequestPayModel
import com.example.domain.entity.ResponsePaySubsModel
import com.example.domain.entity.vote.ResponsePayInAppModel
import com.example.domain.repository.PayRepository
import javax.inject.Inject

class PayRepositoryImpl @Inject constructor(
    private val payDataSource: PayDataSource
) : PayRepository {

    override suspend fun postToCheckSubs(
        request: RequestPayModel
    ): ResponsePaySubsModel? {
        return payDataSource.postToCheckSubsData(
            request.toRequestDto()
        ).data?.toResponsePaySubsModel()
    }

    override suspend fun postToCheckInApp(
        request: RequestPayModel
    ): ResponsePayInAppModel? {
        return payDataSource.postToCheckInAppData(
            request.toRequestDto()
        ).data?.toResponsePayInAppModel()
    }

}