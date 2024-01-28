package com.example.data.datasource.remote

import com.example.data.datasource.ProfileDataSource
import com.example.data.model.request.profile.ProfileModRequestDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileFriendsListDto
import com.example.data.model.response.profile.ResponseProfileUserDto
import com.example.data.remote.service.ProfileService
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val profileService: ProfileService,
) : ProfileDataSource {

    override suspend fun getUserData(): BaseResponse<ResponseProfileUserDto> {
        return profileService.getUserData()
    }

    override suspend fun getFriendsData(
        page: Int,
    ): BaseResponse<ResponseProfileFriendsListDto> {
        return profileService.getFriendsData(page)
    }

    override suspend fun deleteUserData(): BaseResponse<Unit> {
        return profileService.deleteUserData()
    }

    override suspend fun deleteFriendData(
        friendId: Long
    ): BaseResponse<Unit> {
        return profileService.deleteFriendData(friendId)
    }

    override suspend fun postToModUserData(
        request: ProfileModRequestDto
    ): BaseResponse<Unit> {
        return profileService.postToModUserData(request)
    }
}
