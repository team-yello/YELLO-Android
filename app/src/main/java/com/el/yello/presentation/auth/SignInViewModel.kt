package com.el.yello.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.OnboardingRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _postState = MutableLiveData<UiState<ServiceTokenModel?>>()
    val postState: LiveData<UiState<ServiceTokenModel?>> = _postState

    private val _getUserProfileState = MutableLiveData<UiState<Unit>>()
    val getUserProfileState: LiveData<UiState<Unit>>
        get() = _getUserProfileState


    // 서버통신 - 카카오 토큰 보내서 서비스 토큰 받아오기
    fun changeTokenFromServer(accessToken: String, social: String = KAKAO) {
        viewModelScope.launch {
            _postState.value = UiState.Loading
            runCatching {
                onboardingRepository.postTokenToServiceToken(
                    RequestServiceTokenModel(accessToken, social),
                )
            }.onSuccess {
                Timber.d("GET USER DATA SUCCESS : $it")
                if (it == null) {
                    _postState.value = UiState.Empty
                    return@launch
                }
                authRepository.setAutoLogin(it.accessToken, it.refreshToken)
                _postState.value = UiState.Success(it)
            }.onFailure {
                Timber.e("GET USER DATA FAILURE : $it")
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

    // 서버통신 - (가입되어 있는) 유저 정보 가져오기
    fun getUserData() {
        viewModelScope.launch {
            runCatching {
                profileRepository.getUserData()
            }.onSuccess { profile ->
                if (profile == null) {
                    _getUserProfileState.value = UiState.Empty
                    return@launch
                }

                _getUserProfileState.value = UiState.Success(Unit)
                authRepository.setYelloId(profile.yelloId)
            }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("GET USER PROFILE FAILURE : $t")
                        _getUserProfileState.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }

    private companion object {
        const val KAKAO = "KAKAO"
    }
}
