package com.el.yello.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PayUserSubsInfoModel
import com.example.domain.entity.notice.Notice
import com.example.domain.entity.vote.VoteCount
import com.example.domain.repository.AuthRepository
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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val payRepository: PayRepository,
    private val noticeRepository: NoticeRepository,
    private val yelloRepository: YelloRepository,
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

    init {
        putDeviceToken()
        getUserSubscriptionState()
        getNotice()
        getVoteCount()
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
                    } else {
                        _getUserSubsState.value = UiState.Success(userInfo)
                    }
                }
                .onFailure {
                    _getUserSubsState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    private fun getNotice() {
        viewModelScope.launch {
            noticeRepository.getNotice()
                .onSuccess { notice ->
                    if (notice == null) {
                        _getNoticeState.value = UiState.Empty
                        return@onSuccess
                    }

                    Timber.tag("GET_NOTICE_SUCCESS_BEFORE").d(_getNoticeState.value.toString())
                    _getNoticeState.value = UiState.Success(notice)
                    Timber.tag("GET_NOTICE_SUCCESS").d(notice.toString())
                    Timber.tag("GET_NOTICE_SUCCESS_AFTER").d(_getNoticeState.value.toString())
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getNoticeState.value = UiState.Failure(t.code().toString())
                        Timber.tag("GET_NOTICE_FAILURE").e(t)
                        return@onFailure
                    }
                    _getNoticeState.value = UiState.Failure(t.message.toString())
                }
        }
    }

    private fun getVoteCount() {
        viewModelScope.launch {
            yelloRepository.voteCount()
                .onSuccess {
                    if (it != null) {
                        _voteCount.value = UiState.Success(it)
                    }
                }
                .onFailure {
                    _voteCount.value = UiState.Failure(it.message.toString())
                }
        }
    }
}
