package com.example.data.datasource

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileFriendsDto
import com.example.data.model.response.profile.ResponseProfileUserDto

interface ProfileDataSource {

    suspend fun getUserData(
        userId: Int
    ): BaseResponse<ResponseProfileUserDto>

    suspend fun getFriendsData(
        page: Int
    ): BaseResponse<ResponseProfileFriendsDto>

    suspend fun deleteUserData(
    ): BaseResponse<Unit>

}