package com.example.data.datasource

import com.example.data.model.request.event.RequestPostEventStateDto
import com.example.data.model.request.event.RequestRewardAdDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.event.ResponseGetEventDto
import com.example.data.model.response.event.ResponsePostEventDto
import com.example.data.model.response.event.ResponseRewardAdDto
import com.example.data.model.response.event.ResponseRewardAdPossibleDto
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

    suspend fun postRewardAd(
        idempotencyKey: String,
        requestRewardAdDto: RequestRewardAdDto
    ): BaseResponse<ResponseRewardAdDto>

    suspend fun getRewardAdPossible(
        tag: String
    ): BaseResponse<ResponseRewardAdPossibleDto>
}
