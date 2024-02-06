package com.example.domain.repository

import com.example.domain.entity.event.Event

interface EventRepository {
    suspend fun getEvent(): Result<Event?>
}
