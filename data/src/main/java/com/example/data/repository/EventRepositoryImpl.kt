package com.example.data.repository

import com.example.data.datasource.EventDataSource
import com.example.data.model.request.event.RequestPostEventStateDto
import com.example.data.model.request.event.toRequestDto
import com.example.data.model.response.event.ResponseGetEventDto
import com.example.domain.entity.event.Event
import com.example.domain.entity.event.EventResult
import com.example.domain.entity.event.RewardAdModel
import com.example.domain.entity.event.RewardAdPossibleModel
import com.example.domain.entity.event.RewardAdRequestModel
import com.example.domain.repository.EventRepository
import java.util.UUID
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val dataSource: EventDataSource,
) : EventRepository {
    override suspend fun getEvent(): Result<Event?> =
        runCatching {
            dataSource.getEvent().data?.toEvent()
        }

    private fun List<ResponseGetEventDto>?.toEvent(): Event {
        this?.onEach { eventDto ->
            with(eventDto) {
                if (tag == TAG_LUNCH_EVENT && eventReward != null) {
                    return Event(
                        isAvailable = true,
                        title = title,
                        subTitle = subTitle,
                        animationUrlList = animationList,
                        rewardList = eventReward.eventRewardItem.map { rewardItemDto ->
                            rewardItemDto.toReward()
                        },
                    )
                }
            }
        }
        return Event(
            isAvailable = false,
            animationUrlList = emptyList()
        )
    }

    override suspend fun postEventState(idempotencyKey: UUID): Result<Unit> =
        runCatching {
            dataSource.postEventState(
                idempotencyKey = idempotencyKey,
                TAG_LUNCH_EVENT.toRequestPostEventStateDto(),
            )
        }

    private fun String.toRequestPostEventStateDto() = RequestPostEventStateDto(
        tag = this,
    )

    override suspend fun postEvent(idempotencyKey: String): Result<EventResult?> =
        runCatching {
            dataSource.postEvent(idempotencyKey = idempotencyKey).data?.toEventResult()
        }

    override suspend fun postRewardAd(
        idempotencyKey: String,
        rewardAdRequestModel: RewardAdRequestModel
    ): Result<RewardAdModel?> =
        runCatching {
            dataSource.postRewardAd(
                idempotencyKey,
                rewardAdRequestModel.toRequestDto()
            ).data?.toRewardAdModel()
        }

    override suspend fun getRewardAdPossible(): Result<RewardAdPossibleModel?> =
        runCatching {
            dataSource.getRewardAdPossible(TAG_SHOP).data?.toRewardAdPossibleModel()
        }

    companion object {
        private const val TAG_LUNCH_EVENT = "LUNCH_EVENT"
        private const val TAG_SHOP = "shop"
    }
}
