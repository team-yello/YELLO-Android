package com.example.data.remote.service

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileUserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {

    @GET("/api/v1/user/{userId}")
    suspend fun getUserData(
        @Path("userId") friendId: String
    ): BaseResponse<ResponseProfileUserDto>

}