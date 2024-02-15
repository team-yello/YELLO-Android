package com.el.yello.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PayUserSubsInfoModel
import com.example.domain.entity.event.Event
import com.example.domain.entity.notice.Notice
import com.example.domain.entity.vote.VoteCount
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.EventRepository
import com.example.domain.repository.NoticeRepository
import com.example.domain.repository.PayRepository
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val payRepository: PayRepository,
    private val noticeRepository: NoticeRepository,
    private val yelloRepository: YelloRepository,
    private val eventRepository: EventRepository,
) : ViewModel() {
    private val _getUserSubsState =
        MutableStateFlow<UiState<PayUserSubsInfoModel?>>(UiState.Loading)
    val getUserSubsState: StateFlow<UiState<PayUserSubsInfoModel?>>
        get() = _getUserSubsState

    private val _getNoticeState = MutableStateFlow<UiState<Notice>>(UiState.Loading)
    val getNoticeState: StateFlow<UiState<Notice>>
        get() = _getNoticeState

    private val _voteCount = MutableStateFlow<UiState<VoteCount>>(UiState.Loading)
    val voteCount: StateFlow<UiState<VoteCount>>
        get() = _voteCount

    private val _getEventState = MutableStateFlow<UiState<Event>>(UiState.Loading)
    val getEventState: StateFlow<UiState<Event>> get() = _getEventState

    private var _idempotencyKey: UUID? = null
    val idempotencyKey: UUID get() = requireNotNull(_idempotencyKey)

    init {
        putDeviceToken()
        getUserSubscriptionState()
        getNotice()
        getVoteCount()
        getEventState()
        authRepository.setIsFirstLoginData()
    }

    private fun putDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { addTask ->
            runCatching {
                addTask.result
            }.onSuccess { token ->
                if (authRepository.getDeviceToken() != token) resetDeviceToken(token)
            }
        }
    }

    private fun resetDeviceToken(token: String) {
        authRepository.setDeviceToken(token)
        viewModelScope.launch {
            runCatching {
                authRepository.putDeviceToken(token)
            }.onFailure(Timber::e)
        }
    }

    private fun getUserSubscriptionState() {
        viewModelScope.launch {
            payRepository.getUserSubsInfo()
                .onSuccess { userInfo ->
                    if (userInfo == null) {
                        _getUserSubsState.value = UiState.Empty
                        _getUserSubsState.value = UiState.Loading
                        return@onSuccess
                    }
                    _getUserSubsState.value = UiState.Success(userInfo)
                    _getUserSubsState.value = UiState.Loading
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getUserSubsState.value = UiState.Failure(t.code().toString())
                        _getUserSubsState.value = UiState.Loading
                    }
                }
        }
    }

    private fun getNotice() {
        viewModelScope.launch {
            noticeRepository.getNotice()
                .onSuccess { notice ->
                    if (notice == null) {
                        _getNoticeState.value = UiState.Empty
                        _getNoticeState.value = UiState.Loading
                        return@onSuccess
                    }

                    if (noticeRepository.isDisabledNoticeUrl(notice.imageUrl)) return@onSuccess
                    _getNoticeState.value = UiState.Success(notice)
                    _getNoticeState.value = UiState.Loading
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getNoticeState.value = UiState.Failure(t.code().toString())
                        _getNoticeState.value = UiState.Loading
                        return@onFailure
                    }
                    _getNoticeState.value = UiState.Failure(t.message.toString())
                    _getNoticeState.value = UiState.Loading
                }
        }
    }

    private fun getVoteCount() {
        viewModelScope.launch {
            yelloRepository.voteCount()
                .onSuccess {
                    if (it != null) {
                        _voteCount.value = UiState.Success(it)
                        _voteCount.value = UiState.Loading
                    }
                }
                .onFailure {
                    _voteCount.value = UiState.Failure(it.message.toString())
                    _voteCount.value = UiState.Loading
                }
        }
    }

    private fun getEventState() {
        viewModelScope.launch {
            _idempotencyKey = UUID.randomUUID()
            eventRepository.postEventState(idempotencyKey)
                .onSuccess {
                    getEvent()
                }
        }
    }

    private fun getEvent() {
        viewModelScope.launch {
            eventRepository.getEvent()
                .onSuccess { event ->
                    if (event == null) {
                        _getEventState.value = UiState.Empty
                        _getEventState.value = UiState.Loading
                        return@onSuccess
                    }

                    Timber.tag("GET_EVENT_SUCCESS").d(event.toString())
                    if (!event.isAvailable) {
                        _getEventState.value = UiState.Failure(CODE_UNAVAILABLE_EVENT)
                        _getEventState.value = UiState.Loading
                        return@onSuccess
                    }
                    _getEventState.value = UiState.Success(event)
                    _getEventState.value = UiState.Loading
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getEventState.value = UiState.Failure(t.code().toString())
                        _getEventState.value = UiState.Loading
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
