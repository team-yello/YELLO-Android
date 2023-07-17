package com.example.data.datasource.remote

import com.example.data.datasource.ProfileDataSource
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileFriendsDto
import com.example.data.model.response.profile.ResponseProfileUserDto
import com.example.data.remote.service.ProfileService
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val profileService: ProfileService
) : ProfileDataSource {

    override suspend fun getUserData(
        userId: Int
    ): BaseResponse<ResponseProfileUserDto> {
        return profileService.getUserData(userId)
    }

    override suspend fun getFriendsData(
        page: Int
    ): BaseResponse<ResponseProfileFriendsDto> {
        return profileService.getFriendsData(page)
    }

}