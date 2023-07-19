package com.example.data.repository

import com.example.data.datasource.ProfileDataSource
import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource
) : ProfileRepository {

    override suspend fun getUserData(): ProfileUserModel? {
        return profileDataSource.getUserData().data?.toProfileUserModel()
    }

    override suspend fun getFriendsData(page: Int): ProfileFriendsListModel? {
        return profileDataSource.getFriendsData(page).data?.toProfileFriendsListModel()
    }

    override suspend fun deleteUserData(): Unit {
        return profileDataSource.deleteUserData().data ?: Unit
    }

    override suspend fun deleteFriendData(friendId: Int): Unit {
        return profileDataSource.deleteFriendData(friendId).data ?: Unit
    }

}