package com.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.yello.R
import com.yello.databinding.ActivitySignInBinding
import timber.log.Timber

class SignInActivity : BindingActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private lateinit var appLoginCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var accountLoginCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var serviceTermsList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 진행
        binding.btnSignIn.setOnSingleClickListener {
            setAppLoginCallback()
            setAccountLoginCallback()
            setServiceTerms()
            startKakaoLogin()
            getUserInfo()
        }
    }

    // 계정 로그인 callback 구성
    private fun setAccountLoginCallback() {
        accountLoginCallback = { token, error ->
            if (error != null) {
                Timber.tag("auth").e(error, "카카오계정으로 로그인 실패")
            } else if (token != null) {
                Timber.tag("auth").i("카카오계정으로 로그인 성공 %s", token.accessToken)
                startSocialSyncActivity()
            }
        }
    }

    // 앱 로그인 callback 구성
    private fun setAppLoginCallback() {
        appLoginCallback = { token, error ->
            if (error != null) {
                Timber.tag("auth").e(error, "카카오톡으로 로그인 실패")

                // 뒤로가기 경우 예외 처리
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Timber.tag("auth").e(error, "유저가 로그인 취소")
                } else {
                    // 카카오톡 연결 실패 시, 계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        binding.root.context,
                        callback = accountLoginCallback
                    )
                }

            } else if (token != null) {
                Timber.tag("auth").i("카카오톡으로 로그인 성공 %s", token.accessToken)
                startSocialSyncActivity()
            }
        }
    }

    // 카카오 로그인 동의 화면에 포함할 서비스 약관을 지정
    private fun setServiceTerms() {
        serviceTermsList =
            listOf("profile_nickname", "profile_image", "account_email", "age_range")
    }

    // 카카오톡 앱 유무에 따라 로그인 진행
    private fun startKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(binding.root.context)) {
            UserApiClient.instance.loginWithKakaoTalk(
                context = binding.root.context,
                callback = appLoginCallback,
                serviceTerms = serviceTermsList
            )
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                context = binding.root.context,
                callback = accountLoginCallback,
                serviceTerms = serviceTermsList
            )
        }
    }

    private fun startSocialSyncActivity() {
        val intent = Intent(binding.root.context, SocialSyncActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    // 유저 추가 정보 받아오기 - 보류중
    private fun getUserInfo() {
        UserApiClient.instance.me { user, error ->
            var userNickname = user?.kakaoAccount?.profile?.nickname
        }
    }
}