package com.el.yello.presentation.auth

import android.content.Context
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
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import com.el.yello.presentation.auth.SignInActivity.Companion.NOT_SIGNED_IN
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
    val getUserProfileState: LiveData<UiState<Unit>> = _getUserProfileState

    private val _getKakaoDataState = MutableLiveData<UiState<User?>>()
    val getKakaoDataState: LiveData<UiState<User?>> = _getKakaoDataState

    private val serviceTermsList = listOf(THUMBNAIL, EMAIL, FRIEND_LIST)

    // 웹 로그인 실행
    fun loginWithWebCallback(
        context: Context,
        callback: (token: OAuthToken?, error: Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoAccount(
            context = context,
            callback = callback,
            serviceTerms = serviceTermsList,
        )
    }

    // 앱 로그인 실행
    fun loginWithAppCallback(
        context: Context,
        callback: (token: OAuthToken?, error: Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoTalk(
            context = context,
            callback = callback,
            serviceTerms = serviceTermsList,
        )
    }

    // 카카오 통신 - 카카오 유저 정보 받아오기
    fun getKakaoInfo() {
        UserApiClient.instance.me { user, _ ->
            _getKakaoDataState.value = UiState.Loading
            try {
                if (user != null) {
                    _getKakaoDataState.value = UiState.Success(user)
                    return@me
                }
            } catch (e: IllegalArgumentException) {
                _getKakaoDataState.value = UiState.Failure(e.message ?: "")
            }
        }
        _getKakaoDataState.value = UiState.Failure("카카오 정보 불러오기 실패")
    }

    // 서버통신 - 카카오 토큰 보내서 서비스 토큰 받아오기
    fun changeTokenFromServer(accessToken: String, social: String = KAKAO) {
        viewModelScope.launch {
            _postState.value = UiState.Loading
            runCatching {
                onboardingRepository.postTokenToServiceToken(
                    RequestServiceTokenModel(accessToken, social),
                )
            }.onSuccess {
                if (it == null) {
                    _postState.value = UiState.Empty
                    return@launch
                }
                authRepository.setAutoLogin(it.accessToken, it.refreshToken)
                _postState.value = UiState.Success(it)
            }.onFailure {
                if (it is HttpException && it.code() == 403) {
                    _postState.value = UiState.Failure(NOT_SIGNED_IN)
                } else {
                    _postState.value = UiState.Failure("ERROR")
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
                        _getUserProfileState.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }

    private companion object {
        const val KAKAO = "KAKAO"

        const val THUMBNAIL = "profile_image"
        const val EMAIL = "account_email"
        const val FRIEND_LIST = "friends"
    }
}
