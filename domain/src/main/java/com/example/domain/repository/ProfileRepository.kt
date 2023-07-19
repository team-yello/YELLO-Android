package com.example.domain.repository

import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileUserModel

interface ProfileRepository {

    suspend fun getUserData(
    ): ProfileUserModel?

    suspend fun getFriendsData(
        page: Int
    ): ProfileFriendsListModel?

    suspend fun deleteUserData(
    ): Unit

    suspend fun deleteFriendData(
        friendId: Int
    ): Unit

}