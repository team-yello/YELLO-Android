package com.example.domain.repository

import com.example.domain.entity.PayInAppModel
import com.example.domain.entity.PayInfoModel
import com.example.domain.entity.PayRequestModel
import com.example.domain.entity.PaySubsModel
import com.example.domain.entity.PaySubsNeededModel

interface PayRepository {

    suspend fun postToCheckSubs(
        request: PayRequestModel
    ): Result<PaySubsModel?>

    suspend fun postToCheckInApp(
        request: PayRequestModel
    ): Result<PayInAppModel?>

    suspend fun getSubsNeeded(
    ): Result<PaySubsNeededModel?>

    suspend fun getPurchaseInfo(
    ): Result<PayInfoModel?>

}
