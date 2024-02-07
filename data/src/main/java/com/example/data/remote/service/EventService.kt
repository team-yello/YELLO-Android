package com.example.data.remote.service

import com.example.data.model.request.event.RequestPostEventStateDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.event.ResponseGetEventDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventService {
    @GET("/api/v1/event")
    suspend fun getEvent(): BaseResponse<List<ResponseGetEventDto>>

    @POST("/api/v1/event")
    suspend fun postEventState(
        @Body requestPostEventStateDto: RequestPostEventStateDto,
    ): BaseResponse<Unit>
}
