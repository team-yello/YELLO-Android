package com.example.data.remote.service

import com.example.data.model.request.auth.RequestDeviceDto
import com.example.data.model.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.PUT

interface AuthService {
    @PUT("api/v1/user/device")
    suspend fun putDeviceToken(
        @Body body: RequestDeviceDto
    ): BaseResponse<Unit>
}