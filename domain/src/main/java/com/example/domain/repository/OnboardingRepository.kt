package com.example.domain.repository

import com.example.domain.entity.AuthTokenRequestModel
import com.example.domain.entity.AuthTokenModel
import com.example.domain.entity.onboarding.AddFriendListModel
import com.example.domain.entity.onboarding.GroupHighSchool
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.HighSchoolList
import com.example.domain.entity.onboarding.RequestAddFriendModel
import com.example.domain.entity.onboarding.SchoolList
import com.example.domain.entity.onboarding.SignupInfo
import com.example.domain.entity.onboarding.UserInfo

interface OnboardingRepository {
    suspend fun postTokenToServiceToken(
        authTokenRequestModel: AuthTokenRequestModel,
    ): Result<AuthTokenModel?>

    suspend fun getSchoolList(
        keyword: String,
        page: Long,
    ): Result<SchoolList?>

    suspend fun getHighSchoolList(
        keyword: String,
        page: Long,
    ): Result<HighSchoolList?>

    suspend fun getGroupList(
        page: Int,
        name: String,
        search: String,
    ): Result<GroupList?>

    suspend fun getGroupHighSchool(
        name: String,
        keyword: String,
    ): Result<GroupHighSchool?>

    suspend fun getValidYelloId(
        yelloId: String,
    ): Result<Boolean?>

    suspend fun postToGetFriendList(
        request: RequestAddFriendModel,
        page: Int,
    ): Result<AddFriendListModel?>

    suspend fun postSignup(
        signupInfo: SignupInfo,
    ): Result<UserInfo?>
}
