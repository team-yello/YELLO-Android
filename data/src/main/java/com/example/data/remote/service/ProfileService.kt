package com.example.data.remote.service

import com.example.data.model.request.profile.ProfileModRequestDto
import com.example.data.model.request.profile.RequestQuitReasonDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileFriendsListDto
import com.example.data.model.response.profile.ResponseProfileModValidDto
import com.example.data.model.response.profile.ResponseProfileUserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @GET("/api/v2/user")
    suspend fun getUserData(): BaseResponse<ResponseProfileUserDto>

    @GET("/api/v1/friend")
    suspend fun getFriendsData(
        @Query("page") page: Int,
    ): BaseResponse<ResponseProfileFriendsListDto>

    @HTTP(method = "DELETE", path = "/api/v2/user", hasBody = true)
    suspend fun deleteUserData(
        @Body request: RequestQuitReasonDto,
    ): BaseResponse<Unit>

    @DELETE("/api/v1/friend/{friendId}")
    suspend fun deleteFriendData(
        @Path("friendId") friendId: Long,
    ): BaseResponse<Unit>

    @POST("/api/v1/user")
    suspend fun postToModUserData(
        @Body request: ProfileModRequestDto,
    ): BaseResponse<Unit>

    @GET("/api/v1/user/data/account-updated-at")
    suspend fun getModValidData(): BaseResponse<ResponseProfileModValidDto>
}
