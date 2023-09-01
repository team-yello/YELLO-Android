package com.example.data.remote.service

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileFriendsListDto
import com.example.data.model.response.profile.ResponseProfileUserDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @GET("/api/v1/user")
    suspend fun getUserData(
    ): BaseResponse<ResponseProfileUserDto>

    @GET("/api/v1/friend")
    suspend fun getFriendsData(
        @Query("page") page: Int
    ): BaseResponse<ResponseProfileFriendsListDto>

    @DELETE("/api/v1/user")
    suspend fun deleteUserData(
    ): BaseResponse<Unit>

    @DELETE("/api/v1/friend/{friendId}")
    suspend fun deleteFriendData(
        @Path("friendId") friendId: Int
    ): BaseResponse<Unit>

}