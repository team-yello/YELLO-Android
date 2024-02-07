package com.el.yello.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.event.Event
import com.example.domain.repository.EventRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {
    private val _getEventState = MutableStateFlow<UiState<Event>>(UiState.Loading)
    val getEventState: StateFlow<UiState<Event>> get() = _getEventState

    init {
        getEvent()
    }

    private fun getEvent() {
        viewModelScope.launch {
            eventRepository.getEvent()
                .onSuccess { event ->
                    if (event == null) {
                        _getEventState.value = UiState.Empty
                        return@onSuccess
                    }

                    Timber.tag("GET_EVENT_SUCCESS").d(event.toString())
                    if (!event.isAvailable) {
                        _getEventState.value = UiState.Failure(CODE_UNAVAILABLE_EVENT)
                        return@onSuccess
                    }
                    _getEventState.value = UiState.Success(event)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        UiState.Failure(t.code().toString())
                        Timber.tag("GET_EVENT_FAILURE").e(t)
                        return@onFailure
                    }
                    Timber.tag("GET_EVENT_ERROR").e(t)
                }
        }
    }

    companion object {
        const val CODE_UNAVAILABLE_EVENT = "100"
    }
}
