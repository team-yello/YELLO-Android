package com.example.data.remote.service

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.event.ResponseGetEventDto
import retrofit2.http.GET

interface EventService {
    @GET("/api/v1/event")
    suspend fun getEvent(): BaseResponse<List<ResponseGetEventDto>>
}
