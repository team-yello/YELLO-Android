package com.el.yello.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PayUserSubsInfoModel
import com.example.domain.entity.vote.VoteCount
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.PayRepository
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val payRepository: PayRepository,
    private val yelloRepository: YelloRepository,
) : ViewModel() {

    // TODO : State 클래스 하나로 병합
    private val _getUserSubsInfoState =
        MutableStateFlow<UiState<PayUserSubsInfoModel?>>(UiState.Empty)
    val getUserSubsInfoState: StateFlow<UiState<PayUserSubsInfoModel?>> = _getUserSubsInfoState

    private val _voteCount = MutableStateFlow<UiState<VoteCount>>(UiState.Loading)
    val voteCount: StateFlow<UiState<VoteCount>> = _voteCount

    init {
        putDeviceToken()
        getUserSubscriptionState()
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
                        _getUserSubsInfoState.value = UiState.Empty
                    } else {
                        _getUserSubsInfoState.value = UiState.Success(userInfo)
                    }
                }
                .onFailure {
                    _getUserSubsInfoState.value = UiState.Failure(it.message.toString())
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
