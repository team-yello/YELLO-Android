package com.el.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivitySignInBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.onboarding.StartAppActivity
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignInActivity : BindingActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private lateinit var appLoginCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var webLoginCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var deviceToken: String

    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSignInBtnListener()
        viewModel.getDeviceToken()
        observeDeviceTokenState()
        observeKakaoUserDataState()
        observeChangeTokenState()
        observeUserDataExists()
    }

    private fun initSignInBtnListener() {
        binding.btnSignIn.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_onboarding_kakao")
            setWebLoginCallback()
            setAppLoginCallback()
            startKakaoLogin()
        }
    }

    // 웹에서 계정 로그인 callback 구성
    private fun setWebLoginCallback() {
        webLoginCallback = { token, error ->
            if (error != null && token != null) {
                viewModel.changeTokenFromServer(
                    accessToken = token.accessToken,
                    deviceToken = deviceToken,
                )
            } else {
                Timber.tag(TAG_AUTH).d(getString(R.string.sign_in_error_empty_kakao_token))
            }
        }
    }

    // 카카오톡 앱 로그인 callback 구성
    private fun setAppLoginCallback() {
        appLoginCallback = { token, error ->
            if (error != null) {
                // 뒤로가기 경우 예외 처리
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Timber.tag(TAG_AUTH).e(error, getString(R.string.sign_in_error_cancelled))
                } else {
                    viewModel.loginWithWebCallback(this, webLoginCallback)
                }
            } else if (token != null) {
                viewModel.changeTokenFromServer(
                    accessToken = token.accessToken,
                    deviceToken = deviceToken,
                )
            } else {
                Timber.tag(TAG_AUTH).d(getString(R.string.sign_in_error_empty_kakao_token))
            }
        }
    }

    // 카카오톡 앱 설치 유무에 따라 로그인 진행
    private fun startKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            viewModel.loginWithAppCallback(this, appLoginCallback)
        } else {
            viewModel.loginWithWebCallback(this, webLoginCallback)
        }
    }

    // Firebase에서 디바이스 토큰 받아와 저장
    private fun observeDeviceTokenState() {
        viewModel.getDeviceTokenState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    deviceToken = state.data
                }

                is UiState.Failure -> {
                    toast(getString(R.string.sign_in_error_connection))
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    // 서비스 토큰 교체 서버 통신 결과에 따라서 분기 처리 진행
    private fun observeChangeTokenState() {
        viewModel.postChangeTokenState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    // 200(가입된 아이디): 온보딩 뷰 생략하고 바로 메인 화면으로 이동 위해 유저 정보 받기
                    viewModel.getUserData()
                }

                is UiState.Failure -> {
                    if (state.msg == CODE_NOT_SIGNED_IN || state.msg == CODE_NO_UUID) {
                        // 403, 404 : 온보딩 뷰로 이동 위해 카카오 유저 정보 얻기
                        viewModel.getKakaoInfo()
                    } else {
                        // 401 : 에러 발생
                        toast(getString(R.string.sign_in_error_connection))
                    }
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    // Failure -> 카카오에 등록된 유저 정보 받아온 후 친구목록 동의 화면으로 이동
    private fun observeKakaoUserDataState() {
        viewModel.getKakaoDataState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    startSocialSyncActivity(state.data)
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    // Success -> 서버에 등록된 유저 정보가 있는지 확인 후 메인 액티비티로 이동
    private fun observeUserDataExists() {
        viewModel.getUserProfileState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    if (viewModel.getIsFirstLoginData()) {
                        if (viewModel.isResigned) {
                            startActivity(TutorialAActivity.newIntent(this, false))
                        } else {
                            startMainActivity()
                        }
                    } else {
                        startActivity(Intent(this, StartAppActivity::class.java))
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {}
            }
        }
    }

    private fun startSocialSyncActivity(data: User?) {
        Intent(this, SocialSyncActivity::class.java).apply {
            putExtra(EXTRA_KAKAO_ID, data?.id)
            putExtra(EXTRA_NAME, data?.kakaoAccount?.name)
            putExtra(EXTRA_GENDER, data?.kakaoAccount?.gender.toString())
            putExtra(EXTRA_EMAIL, data?.kakaoAccount?.email)
            putExtra(EXTRA_PROFILE_IMAGE, data?.kakaoAccount?.profile?.profileImageUrl)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    companion object {
        const val EXTRA_KAKAO_ID = "KAKAO_ID"
        const val EXTRA_EMAIL = "KAKAO_EMAIL"
        const val EXTRA_PROFILE_IMAGE = "PROFILE_IMAGE"
        const val EXTRA_NAME = "NAME"
        const val EXTRA_GENDER = "GENDER"

        const val CODE_NOT_SIGNED_IN = "403"
        const val CODE_NO_UUID = "404"

        const val TAG_AUTH = "authSignIn"
    }
}
