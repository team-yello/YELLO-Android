package com.example.data.repository

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.toRequestDto
import com.example.domain.entity.MyDepartment
import com.example.domain.entity.MyId
import com.example.domain.entity.MySchool
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingDataSource: OnboardingDataSource,
) : OnboardingRepository {
    override suspend fun postTokenToServiceToken(requestServiceTokenModel: RequestServiceTokenModel): ServiceTokenModel {
        return onboardingDataSource.postTokenToServiceTokenData(requestServiceTokenModel.toRequestDto())
            .toServiceTokenModel()
    }

    override suspend fun getSchoolService(search: String, page: Long): Result<MySchool> {
        return runCatching {
            onboardingDataSource.getSchoolNameData(
                search,
                page,
            ).data.toMySchool()
        }
    }

    override suspend fun getDepartmentService(
        school: String,
        search: String,
        page: Long,
    ): Result<MyDepartment> {
        return runCatching {
            onboardingDataSource.getDepartmentNameData(
                school,
                search,
                page,
            ).data.toMyDepartment()
        }
    }




}
