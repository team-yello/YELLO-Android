package com.example.data.repository

import com.example.data.datasource.EventDataSource
import com.example.data.model.response.event.ResponseGetEventDto
import com.example.domain.entity.event.Event
import com.example.domain.repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val dataSource: EventDataSource,
) : EventRepository {
    override suspend fun getEvent(): Result<Event?> = kotlin.runCatching {
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
                        defaultLottieJson = animationList[0].lottieRes,
                        openLottieJson = animationList[1].lottieRes,
                        rewardList = eventReward.eventRewardItem.map { rewardItemDto ->
                            rewardItemDto.toReward()
                        },
                    )
                }
            }
        }
        return Event(
            isAvailable = false,
        )
    }

    companion object {
        private const val TAG_LUNCH_EVENT = "LUNCH_EVENT"
    }
}
