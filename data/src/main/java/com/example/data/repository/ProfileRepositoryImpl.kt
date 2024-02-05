package com.example.data.repository

import com.example.data.datasource.ProfileDataSource
import com.example.data.model.request.profile.toRequestDto
import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileModRequestModel
import com.example.domain.entity.ProfileModValidModel
import com.example.domain.entity.ProfileQuitReasonModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
) : ProfileRepository {

    override suspend fun getUserData(): Result<ProfileUserModel?> {
        return runCatching {
            profileDataSource.getUserData().data?.toProfileUserModel()
        }
    }

    override suspend fun getFriendsData(
        page: Int,
    ): Result<ProfileFriendsListModel?> {
        return runCatching {
            profileDataSource.getFriendsData(page).data?.toProfileFriendsListModel()
        }
    }

    override suspend fun deleteUserData(request: ProfileQuitReasonModel): Result<Unit> {
        return runCatching {
            profileDataSource.deleteUserData(request.toRequestDto())
        }
    }

    override suspend fun deleteFriendData(
        friendId: Long,
    ): Result<Unit> {
        return runCatching {
            profileDataSource.deleteFriendData(friendId).data
        }
    }

    override suspend fun postToModUserData(
        request: ProfileModRequestModel,
    ): Result<Unit> {
        return runCatching {
            profileDataSource.postToModUserData(request.toRequestDto())
        }
    }

    override suspend fun getModValidData(): Result<ProfileModValidModel?> {
        return runCatching {
            profileDataSource.getModValidData().data?.toProfileModValidModel()
        }
    }
}
