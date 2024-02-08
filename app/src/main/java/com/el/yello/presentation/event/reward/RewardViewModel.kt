package com.el.yello.presentation.event.reward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.event.EventResult
import com.example.domain.repository.EventRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {
    private val _postEventState = MutableStateFlow<UiState<EventResult>>(UiState.Loading)
    val postEventState: StateFlow<UiState<EventResult>> get() = _postEventState

    fun postEvent(idempotencyKey: String) {
        viewModelScope.launch {
            eventRepository.postEvent(idempotencyKey)
                .onSuccess { eventResult ->
                    if (eventResult == null) {
                        _postEventState.value = UiState.Empty
                        return@onSuccess
                    }

                    _postEventState.value = UiState.Success(eventResult)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _postEventState.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }
}
