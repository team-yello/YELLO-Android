package com.example.domain.repository

import com.example.domain.entity.RequestPayModel
import com.example.domain.entity.ResponsePayInAppModel
import com.example.domain.entity.ResponsePurchaseInfoModel
import com.example.domain.entity.ResponsePaySubsModel
import com.example.domain.entity.ResponseSubsNeededModel

interface PayRepository {

    suspend fun postToCheckSubs(
        request: RequestPayModel
    ): Result<ResponsePaySubsModel?>

    suspend fun postToCheckInApp(
        request: RequestPayModel
    ): Result<ResponsePayInAppModel?>

    suspend fun getSubsNeeded(
    ): Result<ResponseSubsNeededModel?>

    suspend fun getPurchaseInfo(
    ): Result<ResponsePurchaseInfoModel?>

}
