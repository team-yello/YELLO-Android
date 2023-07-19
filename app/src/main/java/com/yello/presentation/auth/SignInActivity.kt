package com.yello.presentation.auth

import android.content.Intent
import android.content.Intent.EXTRA_EMAIL
import android.os.Bundle
import androidx.activity.viewModels
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.yello.R
import com.yello.databinding.ActivitySignInBinding
import com.yello.presentation.main.MainActivity
import com.yello.util.context.yelloSnackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignInActivity : BindingActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private lateinit var appLoginCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var accountLoginCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var serviceTermsList: List<String>
    private lateinit var kakaoAccessToken: String

    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO : 4명 키 해시 모두 받은 다음에 코드 지우기
        val keyHash = Utility.getKeyHash(this)
        Timber.tag(TAG_AUTH).d(keyHash)

        initSignInButtonListener()
        setupGetUserProfileState()
    }

    private fun initSignInButtonListener() {
        binding.btnSignIn.setOnSingleClickListener {
            setServiceTerms()
            setAppLoginCallback()
            setAccountLoginCallback()
            startKakaoLogin()
        }
    }

    private fun setServiceTerms() {
        serviceTermsList = listOf("profile_image", "account_email", "friends")
    }

    // 웹에서 계정 로그인 callback 구성
    private fun setAccountLoginCallback() {
        accountLoginCallback = { token, error ->
            if (error != null) {
                Timber.tag(TAG_AUTH).e(error, getString(R.string.sign_in_error_kakao_account_login))
            } else if (token != null) {
                setDataFromObserver(token)
            } else {
                Timber.tag(TAG_AUTH).d(getString(R.string.sign_in_error_empty_kakao_token))
            }
        }
    }

    // 카카오톡 앱 로그인 callback 구성
    private fun setAppLoginCallback() {
        appLoginCallback = { token, error ->
            if (error != null) {
                Timber.tag(TAG_AUTH).e(error, getString(R.string.sign_in_error_kakao_app_login))

                // 뒤로가기 경우 예외 처리
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Timber.tag(TAG_AUTH).e(error, getString(R.string.sign_in_error_cancelled))
                } else {
                    // 카카오톡 연결 실패 시, 계정으로 로그인 시도
                    loginWithAccountCallback()
                }
            } else if (token != null) {
                setDataFromObserver(token)
            } else {
                Timber.tag(TAG_AUTH).d(getString(R.string.sign_in_error_empty_kakao_token))
            }
        }
    }

    // 웹 로그인 실행
    private fun loginWithAccountCallback() {
        UserApiClient.instance.loginWithKakaoAccount(
            context = this,
            callback = accountLoginCallback,
            serviceTerms = serviceTermsList,
        )
    }

    // 앱 로그인 실행
    private fun loginWithAppCallback() {
        UserApiClient.instance.loginWithKakaoTalk(
            context = this,
            callback = appLoginCallback,
            serviceTerms = serviceTermsList,
        )
    }

    // 카카오톡 앱 설치 유무에 따라 로그인 진행
    private fun startKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            loginWithAppCallback()
        } else {
            loginWithAccountCallback()
        }
    }

    // 뷰모델에서 서비스 메서드 실행 & 옵저버로 토큰 받기
    private fun setDataFromObserver(token: OAuthToken?) {
        observeChangeTokenState()
        postKakaoAccessToken(token)
        viewModel.changeTokenFromServer(kakaoAccessToken)
    }

    private fun observeChangeTokenState() {
        viewModel.postState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    // 500(가입된 아이디): 온보딩 뷰 생략하고 바로 메인 화면으로 이동
                    viewModel.getUserData()
                }

                is UiState.Failure -> {
                    if (state.msg == "403") {
                        // 403(가입되지 않은 아이디): 온보딩 뷰로 이동
                        getKakaoInfo()
                    } else {
                        // 401 : 에러 발생
                        yelloSnackbar(
                            binding.root.rootView,
                            getString(R.string.sign_in_error_connection),
                        )
                    }
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {}
            }
        }
    }

    // TODO: 카카오 토큰값 저장
    private fun postKakaoAccessToken(token: OAuthToken?) {
        if (token != null) {
            kakaoAccessToken = token.accessToken
            return
        }

        yelloSnackbar(binding.root, getString(R.string.msg_error))
        Timber.e("카카오 토큰 받기 실패")
    }

    private fun getKakaoInfo() {
        UserApiClient.instance.me { user, _ ->
            try {
                if (user != null) {
                    Timber.d("KAKAO INFO : $user")
                    Intent(this, SocialSyncActivity::class.java).apply {
                        putExtra(EXTRA_KAKAO_ID, user.id)
                        putExtra(EXTRA_EMAIL, user.kakaoAccount?.email)
                        putExtra(EXTRA_PROFILE_IMAGE, user.kakaoAccount?.profile?.profileImageUrl)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(this)
                    }
                    finish()
                    return@me
                }
            } catch (e: IllegalArgumentException) {
                yelloSnackbar(binding.root, getString(R.string.msg_error))
            }
        }
        Timber.e("카카오 정보 불러오기 실패")
        yelloSnackbar(binding.root, getString(R.string.msg_error))
    }

    private fun setupGetUserProfileState() {
        viewModel.getUserProfileState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    startMainActivity()
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

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    companion object {
        const val EXTRA_KAKAO_ID = "KAKAO_ID"
        const val EXTRA_PROFILE_IMAGE = "PROFILE_IMAGE"

        const val TAG_AUTH = "authSignIn"
    }
}
