package com.el.yello.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.auth.SignInActivity.Companion.CODE_NOT_SIGNED_IN
import com.el.yello.presentation.auth.SignInActivity.Companion.CODE_NO_UUID
import com.example.domain.entity.AuthTokenModel
import com.example.domain.entity.AuthTokenRequestModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.OnboardingRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.Scope
import com.kakao.sdk.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _postChangeTokenState = MutableStateFlow<UiState<AuthTokenModel>>(UiState.Empty)
    val postChangeTokenState: StateFlow<UiState<AuthTokenModel?>> = _postChangeTokenState

    private val _getUserProfileState = MutableStateFlow<UiState<ProfileUserModel>>(UiState.Loading)
    val getUserProfileState: StateFlow<UiState<ProfileUserModel>> = _getUserProfileState

    private val _getKakaoInfoState = MutableStateFlow<UiState<User>>(UiState.Empty)
    val getKakaoInfoState: StateFlow<UiState<User?>> = _getKakaoInfoState

    private val _getKakaoValidState = MutableStateFlow<UiState<List<Scope>>>(UiState.Empty)
    val getKakaoValidState: StateFlow<UiState<List<Scope>>> = _getKakaoValidState

    private val serviceTermsList = listOf(THUMBNAIL, EMAIL, FRIEND_LIST, NAME, GENDER)

    private var deviceToken = String()

    private val _getDeviceTokenError = MutableStateFlow(false)
    val getDeviceTokenError: StateFlow<Boolean> = _getDeviceTokenError

    private val _isAppLoginAvailable = MutableStateFlow(true)
    val isAppLoginAvailable: StateFlow<Boolean> = _isAppLoginAvailable

    var isResigned = false
        private set

    private var webLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.e("login with kakao account error : $error")
            // TODO : 뷰에서 오류 메시지 출력 (토큰 오류 state 설정)
        } else if (token != null) {
            getServiceToken(
                accessToken = token.accessToken,
                deviceToken = deviceToken,
            )
        }
    }

    private var appLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                _isAppLoginAvailable.value = false // TODO : 뒤로가기 state 설정
            }

            Timber.e("login with kakao talk error : $error")
            _isAppLoginAvailable.value = false // TODO : 토큰 오류 state 설정
        } else if (token != null) {
            getServiceToken(
                accessToken = token.accessToken,
                deviceToken = deviceToken,
            )
        }
    }

    fun getKakaoToken(context: Context) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context) && isAppLoginAvailable.value) {
            UserApiClient.instance.loginWithKakaoTalk(
                context = context,
                callback = appLoginCallback,
                serviceTerms = serviceTermsList,
            )
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                context = context,
                callback = webLoginCallback,
                serviceTerms = serviceTermsList,
            )
        }
    }

    private fun getServiceToken(
        accessToken: String,
        social: String = KAKAO,
        deviceToken: String,
    ) {
        _postChangeTokenState.value = UiState.Loading
        viewModelScope.launch {
            onboardingRepository.postTokenToServiceToken(
                AuthTokenRequestModel(accessToken, social, deviceToken),
            )
                .onSuccess { authToken ->
                    if (authToken == null) {
                        _postChangeTokenState.value = UiState.Empty
                        return@launch
                    }
                    authRepository.setAutoLogin(authToken.accessToken, authToken.refreshToken)
                    isResigned = authToken.isResigned
                    _postChangeTokenState.value = UiState.Success(authToken)
                }
                .onFailure {
                    if (it is HttpException) {
                        val errorMessage = when (it.code()) {
                            // TODO : 분기 처리 가독성 개선하기
                            403 -> CODE_NOT_SIGNED_IN
                            404 -> CODE_NO_UUID
                            else -> ERROR
                        }
                        _postChangeTokenState.value = UiState.Failure(errorMessage)
                    }
                }
        }
    }

    fun getUserDataFromServer() {
        _getUserProfileState.value = UiState.Loading
        viewModelScope.launch {
            profileRepository.getUserData()
                .onSuccess { profile ->
                    if (profile == null) {
                        _getUserProfileState.value = UiState.Empty
                        return@onSuccess
                    }

                    _getUserProfileState.value = UiState.Success(profile)
                    authRepository.setYelloId(profile.yelloId)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getUserProfileState.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }

    fun getKakaoInfo() {
        UserApiClient.instance.me { user, _ ->
            _getKakaoInfoState.value = UiState.Loading
            try {
                if (user != null) {
                    _getKakaoInfoState.value = UiState.Success(user)
                    return@me
                }
            } catch (e: IllegalArgumentException) {
                _getKakaoInfoState.value = UiState.Failure(e.message.toString())
            }
        }
    }

    fun checkFriendsListValid() {
        val scopes = mutableListOf(FRIEND_LIST)
        _getKakaoValidState.value = UiState.Loading
        UserApiClient.instance.scopes(scopes) { scopeInfo, error ->
            if (error != null) {
                _getKakaoValidState.value = UiState.Failure(error.message.toString())
            } else if (scopeInfo != null) {
                _getKakaoValidState.value = UiState.Success(scopeInfo.scopes ?: listOf())
            } else {
                _getKakaoValidState.value = UiState.Failure(ERROR)
            }
        }
    }

    private fun storeDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result.isNotEmpty()) {
                deviceToken = task.result
                authRepository.setDeviceToken(deviceToken)
            } else {
                _getDeviceTokenError.value = true
            }
        }
    }

    fun getIsFirstLoginData() = authRepository.getIsFirstLoginData()

    companion object {
        const val KAKAO = "KAKAO"

        const val THUMBNAIL = "profile_image"
        const val EMAIL = "account_email"
        const val FRIEND_LIST = "friends"
        const val NAME = "name"
        const val GENDER = "gender"

        const val ERROR = "error"
    }
}
