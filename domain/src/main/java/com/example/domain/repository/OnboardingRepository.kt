package com.example.domain.repository

import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel

interface OnboardingRepository {

    suspend fun postTokenToServiceToken(requestServiceTokenModel: RequestServiceTokenModel): ServiceTokenModel

}