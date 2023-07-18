package com.example.domain.repository

import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.entity.onboarding.FriendGroup
import com.example.domain.entity.onboarding.MyDepartment
import com.example.domain.entity.onboarding.Friend
import com.example.domain.entity.onboarding.FriendList
import com.example.domain.entity.onboarding.MySchool

interface OnboardingRepository {
    suspend fun postTokenToServiceToken(requestServiceTokenModel: RequestServiceTokenModel): ServiceTokenModel

    suspend fun getSchoolService(
        search: String,
        page: Long,
    ): Result<MySchool?>

    suspend fun getDepartmentService(
        school: String,
        search: String,
        page: Long,
    ): Result<MyDepartment?>

    suspend fun getIdService(
        yelloId: String,
    ): Result<Boolean?>

    suspend fun postFriendService(
        friendGroup: FriendGroup,
        page: Long,
    ): Result<FriendList?>
}
