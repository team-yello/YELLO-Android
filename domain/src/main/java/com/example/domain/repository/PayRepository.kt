package com.example.domain.repository

import com.example.domain.entity.RequestPayModel
import com.example.domain.entity.ResponsePayCheckModel
import com.example.domain.entity.ResponsePaySubsModel
import com.example.domain.entity.ResponsePayInAppModel

interface PayRepository {

    suspend fun postToCheckSubs(
        request: RequestPayModel
    ): ResponsePaySubsModel?

    suspend fun postToCheckInApp(
        request: RequestPayModel
    ): ResponsePayInAppModel?

    suspend fun getIsSubscribed(
    ): ResponsePayCheckModel?

}
