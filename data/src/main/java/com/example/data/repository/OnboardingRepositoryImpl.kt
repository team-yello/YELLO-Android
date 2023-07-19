package com.example.data.repository

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.toRequestDto
import com.example.data.model.request.onboarding.toRequestPostSignupDto
import com.example.data.model.request.onboarding.toRequestSignFriendDto
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.entity.onboarding.FriendGroup
import com.example.domain.entity.onboarding.FriendList
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.SchoolList
import com.example.domain.entity.onboarding.SignupInfo
import com.example.domain.entity.onboarding.UserInfo
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

    override suspend fun getSchoolList(search: String, page: Int): Result<SchoolList?> {
        return runCatching {
            onboardingDataSource.getSchoolNameData(
                search,
                page,
            ).data?.toMySchool()
        }
    }

    override suspend fun getGroupList(
        school: String,
        search: String,
        page: Int,
    ): Result<GroupList?> {
        return runCatching {
            onboardingDataSource.getDepartmentNameData(
                school,
                search,
                page,
            ).data?.toMyDepartment()
        }
    }

    override suspend fun getValidYelloId(yelloId: String): Result<Boolean?> {
        return runCatching {
            onboardingDataSource.getValidYelloId(
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

    override suspend fun postSignup(signupInfo: SignupInfo): Result<UserInfo?> = runCatching {
        onboardingDataSource.postSignup(
            signupInfo.toRequestPostSignupDto(),
        ).data?.toUserInfo()
    }
}
