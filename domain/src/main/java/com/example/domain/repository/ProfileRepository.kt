package com.example.domain.repository

import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileModRequestModel
import com.example.domain.entity.ProfileModValidModel
import com.example.domain.entity.ProfileQuitReasonModel
import com.example.domain.entity.ProfileUserModel

interface ProfileRepository {

    suspend fun getUserData(): Result<ProfileUserModel?>

    suspend fun getFriendsData(
        page: Int,
    ): Result<ProfileFriendsListModel?>

    suspend fun deleteUserData(
        request: ProfileQuitReasonModel,
    ): Result<Unit>

    suspend fun deleteFriendData(
        friendId: Long,
    ): Result<Unit>

    suspend fun postToModUserData(
        request: ProfileModRequestModel,
    ): Result<Unit>

    suspend fun getModValidData(): Result<ProfileModValidModel?>
}
