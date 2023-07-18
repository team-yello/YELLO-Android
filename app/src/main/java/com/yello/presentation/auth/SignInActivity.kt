package com.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
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
                Timber.tag(TAG_AUTH).e(error, "카카오계정으로 로그인 실패")
            } else if (token != null) {
                setDataFromObserver(token)
            } else {
                Timber.tag(TAG_AUTH).d("빈 카카오 토큰")
            }
        }
    }

    // 카카오톡 앱 로그인 callback 구성
    private fun setAppLoginCallback() {
        appLoginCallback = { token, error ->
            if (error != null) {
                Timber.tag(TAG_AUTH).e(error, "카카오톡으로 로그인 실패")

                // 뒤로가기 경우 예외 처리
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Timber.tag(TAG_AUTH).e(error, "유저가 로그인 취소")
                } else {
                    // 카카오톡 연결 실패 시, 계정으로 로그인 시도
                    loginWithAccountCallback()
                }
            } else if (token != null) {
                setDataFromObserver(token)
            } else {
                Timber.tag(TAG_AUTH).d("빈 카카오 토큰")
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
                    // TODO: 서비스 토큰 값 저장
                    val serviceAccessToken = state.data?.accessToken
                    startMainActivity()
                }

                is UiState.Failure -> {
                    if (state.msg == "403") {
                        getKakaoInfo()
                        startSocialSyncActivity()
                    } else {
                        toast("서버 통신에 실패했습니다")
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
            if (user != null) {
                viewModel.setKakaoInfo(
                    kakaoId = user.id.toString(),
                    email = user.kakaoAccount?.email ?: "",
                    profileImage = user.kakaoAccount?.profile?.thumbnailImageUrl ?: "",
                )
                return@me
            }

            Timber.e("카카오 정보 불러오기 실패")
            yelloSnackbar(binding.root, getString(R.string.msg_error))
        }
    }

    private fun startSocialSyncActivity() {
        Intent(this, SocialSyncActivity::class.java).apply {
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

    private companion object {
        const val TAG_AUTH = "authSignIn"
    }
}
