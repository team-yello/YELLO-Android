package com.example.domain.repository

import com.example.domain.entity.event.RewardAdModel
import com.example.domain.entity.event.RewardAdRequestModel
import com.example.domain.entity.event.Event
import com.example.domain.entity.event.EventResult
import com.example.domain.entity.event.RewardAdPossibleModel
import java.util.UUID

interface EventRepository {
    suspend fun getEvent(): Result<Event?>

    suspend fun postEventState(idempotencyKey: UUID): Result<Unit>

    suspend fun postEvent(idempotencyKey: String): Result<EventResult?>

    suspend fun postRewardAd(
        idempotencyKey: String,
        rewardAdRequestModel: RewardAdRequestModel
    ): Result<RewardAdModel?>

    suspend fun getRewardAdPossible(): Result<RewardAdPossibleModel?>
}
