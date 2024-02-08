package com.example.domain.repository

import com.example.domain.entity.event.Event
import com.example.domain.entity.event.EventResult
import java.util.UUID

interface EventRepository {
    suspend fun getEvent(): Result<Event?>

    suspend fun postEventState(idempotencyKey: UUID): Result<Unit>

    suspend fun postEvent(idempotencyKey: String): Result<EventResult?>
}
