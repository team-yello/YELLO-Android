package com.example.data.datasource.remote

import com.example.data.datasource.EventDataSource
import com.example.data.model.request.event.RequestPostEventStateDto
import com.example.data.model.request.event.RequestRewardAdDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.event.ResponseGetEventDto
import com.example.data.model.response.event.ResponsePostEventDto
import com.example.data.model.response.event.ResponseRewardAdDto
import com.example.data.model.response.event.ResponseRewardAdPossibleDto
import com.example.data.remote.service.EventService
import java.util.UUID
import javax.inject.Inject

class EventDataSourceImpl @Inject constructor(
    private val service: EventService,
) : EventDataSource {
    override suspend fun getEvent(): BaseResponse<List<ResponseGetEventDto>> =
        service.getEvent()

    override suspend fun postEventState(
        idempotencyKey: UUID,
        requestPostEventStateDto: RequestPostEventStateDto,
    ): BaseResponse<Unit> =
        service.postEventState(
            idempotencyKey,
            requestPostEventStateDto,
        )

    override suspend fun postEvent(idempotencyKey: String): BaseResponse<ResponsePostEventDto> =
        service.postEvent(idempotencyKey)

    override suspend fun postRewardAd(
        idempotencyKey: String,
        requestRewardAdDto: RequestRewardAdDto
    ): BaseResponse<ResponseRewardAdDto> =
        service.postRewardAd(
            idempotencyKey,
            requestRewardAdDto
        )

    override suspend fun getRewardAdPossible(
        tag: String
    ): BaseResponse<ResponseRewardAdPossibleDto> =
        service.getRewardAdPossible(tag)
}
