package com.el.yello.presentation.event

import androidx.lifecycle.ViewModel
import com.example.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {
    private var idempotencyKey = ""

    fun setIdempotencyKey(key: String) {
        idempotencyKey = key
    }

    fun getIdempotencyKey() = idempotencyKey
}
