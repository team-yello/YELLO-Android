package com.yello.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.repository.OnboardingRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _postState = MutableLiveData<UiState<ServiceTokenModel>>()
    val postState: LiveData<UiState<ServiceTokenModel>> = _postState

    fun changeTokenFromServer() {
        viewModelScope.launch {
            _postState.value = UiState.Loading
            runCatching {
                onboardingRepository.postTokenToServiceToken(
                    RequestServiceTokenModel("q", "q")
                )
            }.onSuccess {
                _postState.value = UiState.Success(it)
            }.onFailure {
                _postState.value = UiState.Failure("서비스 토큰 교체 서버 통신 실패")
            }
        }
    }
}