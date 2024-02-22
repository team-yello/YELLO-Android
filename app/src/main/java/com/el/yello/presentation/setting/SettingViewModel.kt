package com.el.yello.presentation.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.ProfileQuitReasonModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.state.UiState
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _kakaoLogoutState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val kakaoLogoutState: StateFlow<UiState<Unit>> = _kakaoLogoutState

    private val _kakaoQuitState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val kakaoQuitState: StateFlow<UiState<Unit>> = _kakaoQuitState

    private val _deleteUserState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val deleteUserState: StateFlow<UiState<Unit>> = _deleteUserState

    var quitReasonText = ""
    val etcText = MutableLiveData("")

    fun logoutKakaoAccount() {
        UserApiClient.instance.logout { error ->
            _kakaoLogoutState.value = UiState.Loading
            if (error == null) {
                _kakaoLogoutState.value = UiState.Success(Unit)
                clearLocalInfo()
            } else {
                _kakaoLogoutState.value = UiState.Failure(error.message.toString())
            }
        }
    }

    fun deleteUserDataToServer() {
        viewModelScope.launch {
            _deleteUserState.value = UiState.Loading
            val quitReason = if (quitReasonText == "기타") {
                etcText.value.toString()
            } else {
                quitReasonText
            }
            profileRepository.deleteUserData(
                ProfileQuitReasonModel(
                    quitReason,
                ),
            )
                .onSuccess {
                    clearLocalInfo()
                    delay(300)
                    _deleteUserState.value = UiState.Success(it)
                }
                .onFailure {
                    _deleteUserState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun quitKakaoAccount() {
        UserApiClient.instance.unlink { error ->
            _kakaoQuitState.value = UiState.Loading
            if (error == null) {
                _kakaoQuitState.value = UiState.Success(Unit)
            } else {
                _kakaoQuitState.value = UiState.Failure(error.message.toString())
            }
        }
    }

    private fun clearLocalInfo() {
        authRepository.clearLocalPref()
    }

}