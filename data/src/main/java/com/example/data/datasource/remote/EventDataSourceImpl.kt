package com.example.data.datasource.remote

import com.example.data.datasource.EventDataSource
import com.example.data.model.request.event.RequestPostEventStateDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.event.ResponseGetEventDto
import com.example.data.remote.service.EventService
import javax.inject.Inject

class EventDataSourceImpl @Inject constructor(
    private val service: EventService,
) : EventDataSource {
    override suspend fun getEvent(): BaseResponse<List<ResponseGetEventDto>> =
        service.getEvent()

    override suspend fun postEventState(requestPostEventStateDto: RequestPostEventStateDto): BaseResponse<Unit> =
        service.postEventState(requestPostEventStateDto)
}
