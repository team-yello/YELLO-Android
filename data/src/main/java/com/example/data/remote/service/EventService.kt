package com.example.data.remote.service

import com.example.data.model.request.event.RequestPostEventStateDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.event.ResponseGetEventDto
import com.example.data.model.response.event.ResponsePostEventDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.UUID

interface EventService {
    @GET("/api/v1/event")
    suspend fun getEvent(): BaseResponse<List<ResponseGetEventDto>>

    @POST("/api/v1/event")
    suspend fun postEventState(
        @Header("IdempotencyKey")
        idempotencyKey: UUID,
        @Body requestPostEventStateDto: RequestPostEventStateDto,
    ): BaseResponse<Unit>

    @POST("/api/v1/event/reward")
    suspend fun postEvent(
        @Header("IdempotencyKey")
        idempotencyKey: String,
    ): BaseResponse<ResponsePostEventDto>
}
