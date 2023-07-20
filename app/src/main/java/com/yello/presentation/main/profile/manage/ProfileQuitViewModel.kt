package com.yello.presentation.main.profile.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileQuitViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _deleteUserState = MutableLiveData<UiState<Unit>>()
    val deleteUserState: LiveData<UiState<Unit>> = _deleteUserState

    // 서버 통신 - 회원 탈퇴
    fun deleteUserDataToServer() {
        viewModelScope.launch {
            _deleteUserState.value = UiState.Loading
            runCatching {
                profileRepository.deleteUserData()
            }.onSuccess {
                _deleteUserState.value = UiState.Success(it)
                clearLocalInfo()
            }.onFailure {
                _deleteUserState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun clearLocalInfo() {
        authRepository.clearLocalPref()
    }
}