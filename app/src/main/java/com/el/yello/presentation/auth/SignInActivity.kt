package com.el.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivitySignInBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.onboarding.GetAlarmActivity
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.user.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BindingActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSignInBtnListener()
        viewModel.initLoginState()
        viewModel.getDeviceToken()
        observeDeviceTokenError()
        observeAppLoginError()
        observeKakaoUserDataState()
        observeChangeTokenState()
        observeUserDataState()
    }

    // 카카오톡 앱 설치 유무에 따라 로그인 진행
    private fun initSignInBtnListener() {
        binding.btnSignIn.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_onboarding_kakao")
            viewModel.startKakaoLogIn(this)
        }
    }

    private fun observeDeviceTokenError() {
        viewModel.getDeviceTokenError.observe(this) {
            toast(getString(R.string.sign_in_error_connection))
        }
    }

    // 카카오통 앱 로그인에 실패한 경우 웹 로그인 시도
    private fun observeAppLoginError() {
        viewModel.isAppLoginAvailable.observe(this) { available ->
            if (!available) viewModel.startKakaoLogIn(this)
        }
    }

    // 서비스 토큰 교체 서버 통신 결과에 따라서 분기 처리 진행
    private fun observeChangeTokenState() {
        viewModel.postChangeTokenState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    // 200(가입된 아이디): 온보딩 뷰 생략하고 바로 메인 화면으로 이동 위해 유저 정보 받기
                    viewModel.getUserDataFromServer()
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
    private fun observeUserDataState() {
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
                        startActivity(Intent(this, GetAlarmActivity::class.java))
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
    }
}
