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
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _postState = MutableLiveData<UiState<ServiceTokenModel?>>()
    val postState: LiveData<UiState<ServiceTokenModel?>> = _postState

    fun changeTokenFromServer(accessToken: String, social: String = "KAKAO") {
        viewModelScope.launch {
            _postState.value = UiState.Loading
            runCatching {
                onboardingRepository.postTokenToServiceToken(
                    RequestServiceTokenModel(accessToken, social)
                )
            }.onSuccess {
                _postState.value = UiState.Success(it)
            }.onFailure {
                if (it is HttpException && it.code() == 403) {
                    _postState.value = UiState.Failure("403")
                } else if (it is HttpException && it.code() == 401) {
                    _postState.value = UiState.Failure("401")
                } else {
                    _postState.value = UiState.Failure("error")
                }
            }
        }
    }
}