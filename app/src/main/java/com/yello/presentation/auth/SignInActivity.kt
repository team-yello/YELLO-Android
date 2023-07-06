package com.yello.presentation.auth

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 진행
        binding.btnSignIn.setOnSingleClickListener {
            setAppLoginCallback()
            setAccountLoginCallback()
            startKakaoLogin()
        }

    }

    // 계정 로그인 callback 구성
    private fun setAccountLoginCallback() {
        accountLoginCallback = { token, error ->
            if (error != null) {
                Timber.tag("auth").e(error, "카카오계정으로 로그인 실패")
            } else if (token != null) {
                Timber.tag("auth").i("카카오계정으로 로그인 성공 %s", token.accessToken)
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
            }
        }
    }

    // 상황 대응해서 로그인 진행
    private fun startKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(binding.root.context)) {
            UserApiClient.instance.loginWithKakaoTalk(
                binding.root.context,
                callback = appLoginCallback
            )
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                binding.root.context,
                callback = accountLoginCallback
            )
        }
    }
}