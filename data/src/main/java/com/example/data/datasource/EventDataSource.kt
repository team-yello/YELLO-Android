package com.example.data.datasource

import com.example.data.model.request.event.RequestPostEventStateDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.event.ResponseGetEventDto

interface EventDataSource {
    suspend fun getEvent(): BaseResponse<List<ResponseGetEventDto>>

    suspend fun postEventState(requestPostEventStateDto: RequestPostEventStateDto): BaseResponse<Unit>
}
