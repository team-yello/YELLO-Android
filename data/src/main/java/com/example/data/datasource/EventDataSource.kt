package com.example.data.datasource

import com.example.data.model.request.event.RequestPostEventStateDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.event.ResponseGetEventDto
import com.example.data.model.response.event.ResponsePostEventDto
import java.util.UUID

interface EventDataSource {
    suspend fun getEvent(): BaseResponse<List<ResponseGetEventDto>>

    suspend fun postEventState(
        idempotencyKey: UUID,
        requestPostEventStateDto: RequestPostEventStateDto,
    ): BaseResponse<Unit>

    suspend fun postEvent(
        idempotencyKey: String,
    ): BaseResponse<ResponsePostEventDto>
}
