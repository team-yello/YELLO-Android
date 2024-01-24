package com.el.yello.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _postChangeTokenResult = MutableSharedFlow<Boolean>()
    val postChangeTokenResult: SharedFlow<Boolean> = _postChangeTokenResult

    private val _getUserProfileState = MutableStateFlow<UiState<ProfileUserModel>>(UiState.Empty)
    val getUserProfileState: StateFlow<UiState<ProfileUserModel>> = _getUserProfileState

    private val _getKakaoInfoResult = MutableSharedFlow<Boolean>()
    val getKakaoInfoResult: SharedFlow<Boolean> = _getKakaoInfoResult

    lateinit var kakaoUserInfo: User

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
        if (error == null && token != null) {
            changeTokenFromServer(
                accessToken = token.accessToken,
                deviceToken = deviceToken,
            )
        }
    }

    private var appLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            // 뒤로가기 경우 예외 처리
            if (!(error is ClientError && error.reason == ClientErrorCause.Cancelled)) {
                _isAppLoginAvailable.value = false
            }
        } else if (token != null) {
            changeTokenFromServer(
                accessToken = token.accessToken,
                deviceToken = deviceToken,
            )
        }
    }

    fun initLoginState() {
        _isAppLoginAvailable.value = true
        _getDeviceTokenError.value = false
    }

    fun startKakaoLogIn(context: Context) {
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

    // 카카오 통신 - 카카오 유저 정보 받아오기
    private fun getKakaoInfo() {
        UserApiClient.instance.me { user, _ ->
            try {
                if (user != null) {
                    kakaoUserInfo = user
                    checkFriendsListValid()
                }
            } catch (e: IllegalArgumentException) {
                viewModelScope.launch {
                    _getKakaoInfoResult.emit(false)
                }
            }
        }
    }

    fun checkKakaoUserInfoStored() = ::kakaoUserInfo.isInitialized

    fun isUserNameBlank() =
        !::kakaoUserInfo.isInitialized || kakaoUserInfo.kakaoAccount?.name.isNullOrEmpty()

    private fun checkFriendsListValid() {
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

    // 서버통신 - 카카오 토큰 보내서 서비스 토큰 받아오기
    private fun changeTokenFromServer(
        accessToken: String,
        social: String = KAKAO,
        deviceToken: String
    ) {
        viewModelScope.launch {
            onboardingRepository.postTokenToServiceToken(
                AuthTokenRequestModel(accessToken, social, deviceToken),
            )
                .onSuccess {
                    // 200(가입된 아이디): 온보딩 뷰 생략하고 바로 메인 화면으로 이동 위해 유저 정보 받기
                    if (it == null) return@launch
                    authRepository.setAutoLogin(it.accessToken, it.refreshToken)
                    isResigned = it.isResigned
                    getUserDataFromServer()
                }
                .onFailure {
                    // 403, 404 : 온보딩 뷰로 이동 위해 카카오 유저 정보 얻기
                    if (it is HttpException && (it.code() == 403 || it.code() == 404)) {
                        getKakaoInfo()
                    } else {
                        _postChangeTokenResult.emit(false)
                    }
                }
        }
    }

    // 서버통신 - (가입되어 있는) 유저 정보 가져오기
    private fun getUserDataFromServer() {
        _getUserProfileState.value = UiState.Loading
        viewModelScope.launch {
            profileRepository.getUserData()
                .onSuccess { profile ->
                    if (profile != null) {
                        _getUserProfileState.value = UiState.Success(profile)
                        authRepository.setYelloId(profile.yelloId)
                    }
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getUserProfileState.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }

    // 디바이스 토큰 FCM에서 받아 로컬에 저장
    fun getDeviceToken() {
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
