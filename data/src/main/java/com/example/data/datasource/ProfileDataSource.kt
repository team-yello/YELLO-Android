package com.example.data.datasource

import com.example.data.model.request.profile.ProfileModRequestDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileFriendsListDto
import com.example.data.model.response.profile.ResponseProfileModValidDto
import com.example.data.model.response.profile.ResponseProfileUserDto

interface ProfileDataSource {

    suspend fun getUserData(): BaseResponse<ResponseProfileUserDto>

    suspend fun getFriendsData(
        page: Int,
    ): BaseResponse<ResponseProfileFriendsListDto>

    suspend fun deleteUserData(): BaseResponse<Unit>

    suspend fun deleteFriendData(
        friendId: Long,
    ): BaseResponse<Unit>

    suspend fun postToModUserData(
        request: ProfileModRequestDto
    ): BaseResponse<Unit>

    suspend fun getModValidData() : BaseResponse<ResponseProfileModValidDto>

}
