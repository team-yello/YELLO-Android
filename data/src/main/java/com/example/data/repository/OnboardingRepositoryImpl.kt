package com.example.data.repository

import com.example.data.datasource.OnboardingDataSource
import com.example.data.model.request.onboarding.toRequestDto
import com.example.data.model.request.onboarding.toRequestPostSignupDto
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.entity.onboarding.AddFriendListModel
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.RequestAddFriendModel
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

    override suspend fun getSchoolList(page: Int, search: String): Result<SchoolList?> {
        return runCatching {
            onboardingDataSource.getSchoolNameData(
                page,
                search,
            ).data?.toMySchool()
        }
    }

    override suspend fun getGroupList(
        page: Int,
        school: String,
        search: String,
    ): Result<GroupList?> {
        return runCatching {
            onboardingDataSource.getDepartmentNameData(
                page,
                school,
                search,
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

    override suspend fun postToGetFriendList(
        request: RequestAddFriendModel,
        page: Int,
    ): AddFriendListModel? {
        return onboardingDataSource.postToGetFriendsListData(
            request.toRequestDto(),
            page,
        ).data?.toFriendListModel()
    }

    override suspend fun postSignup(signupInfo: SignupInfo): Result<UserInfo?> = runCatching {
        onboardingDataSource.postSignup(
            signupInfo.toRequestPostSignupDto(),
        ).data?.toUserInfo()
    }
}
