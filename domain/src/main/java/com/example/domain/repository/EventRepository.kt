package com.example.domain.repository

import com.example.domain.entity.event.Event
import java.util.UUID

interface EventRepository {
    suspend fun getEvent(): Result<Event?>

    suspend fun postEventState(idempotencyKey: UUID): Result<Unit>
}
