package com.example.domain.repository

import com.example.domain.entity.RequestOnboardingListModel
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.entity.onboarding.FriendList
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.SchoolList
import com.example.domain.entity.onboarding.SignupInfo
import com.example.domain.entity.onboarding.UserInfo

interface OnboardingRepository {
    suspend fun postTokenToServiceToken(requestServiceTokenModel: RequestServiceTokenModel): ServiceTokenModel?

    suspend fun getSchoolList(
        search: String,
        page: Int,
    ): Result<SchoolList?>

    suspend fun getGroupList(
        school: String,
        search: String,
        page: Int,
    ): Result<GroupList?>

    suspend fun getValidYelloId(
        yelloId: String,
    ): Result<Boolean?>

    suspend fun postToGetFriendList(
        request: RequestOnboardingListModel,
        page: Long,
    ): FriendList?

    suspend fun postSignup(
        signupInfo: SignupInfo,
    ): Result<UserInfo?>
}
