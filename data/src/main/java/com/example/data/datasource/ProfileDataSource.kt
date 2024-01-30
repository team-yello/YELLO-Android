package com.example.data.datasource

import com.example.data.model.request.profile.RequestQuitReasonDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileFriendsListDto
import com.example.data.model.response.profile.ResponseProfileUserDto

interface ProfileDataSource {

    suspend fun getUserData(): BaseResponse<ResponseProfileUserDto>

    suspend fun getFriendsData(
        page: Int,
    ): BaseResponse<ResponseProfileFriendsListDto>

    suspend fun deleteUserData(
        request: RequestQuitReasonDto,
    ): BaseResponse<Unit>

    suspend fun deleteFriendData(
        friendId: Long,
    ): BaseResponse<Unit>
}
