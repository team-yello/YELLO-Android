package com.example.domain.repository

import com.example.domain.entity.ProfileFriendsModel
import com.example.domain.entity.ProfileUserModel

interface ProfileRepository {

    suspend fun getUserData(
        userId: Int
    ): ProfileUserModel?

    suspend fun getFriendsData(
        page: Int
    ): ProfileFriendsModel?

    suspend fun deleteUserData(
    ): Unit

    suspend fun deleteFriendData(
        friendId: Int
    ): Unit

}