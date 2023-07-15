package com.example.data.repository

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.toRequestDto
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingDataSource: OnboardingDataSource
) : OnboardingRepository {

    override suspend fun getServiceToken(requestServiceTokenModel: RequestServiceTokenModel): ServiceTokenModel {
        return onboardingDataSource.getServiceTokenData(requestServiceTokenModel.toRequestDto())
            .toServiceTokenModel()
    }
}