package com.example.data.repository

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.toRequestDto
import com.example.data.model.request.onboarding.toRequestSignFriendDto
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.entity.onboarding.FriendGroup
import com.example.domain.entity.onboarding.FriendList
import com.example.domain.entity.onboarding.MyDepartment
import com.example.domain.entity.onboarding.MySchool
import com.example.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingDataSource: OnboardingDataSource,
) : OnboardingRepository {

    override suspend fun postTokenToServiceToken(requestServiceTokenModel: RequestServiceTokenModel): ServiceTokenModel? {
        return onboardingDataSource.postTokenToServiceTokenData(
            requestServiceTokenModel.toRequestDto(),
        ).data?.toServiceTokenModel()
    }

    override suspend fun getSchoolService(search: String, page: Long): Result<MySchool?> {
        return runCatching {
            onboardingDataSource.getSchoolNameData(
                search,
                page,
            ).data?.toMySchool()
        }
    }

    override suspend fun getDepartmentService(
        school: String,
        search: String,
        page: Long,
    ): Result<MyDepartment?> {
        return runCatching {
            onboardingDataSource.getDepartmentNameData(
                school,
                search,
                page,
            ).data?.toMyDepartment()
        }
    }

    override suspend fun getIdService(yelloId: String): Result<Boolean?> {
        return runCatching {
            onboardingDataSource.getIdValidData(
                yelloId,
            ).data
        }
    }

    override suspend fun postFriendService(
        friendGroup: FriendGroup,
        page: Long,
    ): Result<FriendList?> {
        return runCatching {
            onboardingDataSource.postFriendData(
                friendGroup.toRequestSignFriendDto(),
                page,
            ).data?.toMyFriend()
        }
    }
}
