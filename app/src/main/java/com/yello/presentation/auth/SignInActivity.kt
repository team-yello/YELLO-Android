package com.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
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

        // TODO : 4명 키 해시 모두 받은 다음에 코드 지우기
        val keyHash = Utility.getKeyHash(this)
        Timber.tag("signIn").d(keyHash)

        binding.btnSignIn.setOnSingleClickListener {
            setServiceTerms()
            setAppLoginCallback()
            setAccountLoginCallback()
            startKakaoLogin()
            getUserInfo()
        }
    }


    // TODO : 카카오 로그인 동의 화면에 포함할 서비스 약관 항목 지정 (기획 측에서 필요 약관 확정해서 넘겨주기)
    private fun setServiceTerms() {
        serviceTermsList = listOf("profile_nickname", "profile_image", "account_email", "age_range", "friends")
    }

    // 웹에서 계정 로그인 callback 구성
    private fun setAccountLoginCallback() {
        accountLoginCallback = { token, error ->
            if (error != null) {
                Timber.tag("signIn").e(error, "카카오계정으로 로그인 실패")
            } else if (token != null) {
                postKakaoAccessToken(token)
                startSocialSyncActivity()
            }
        }
    }

    private fun loginWithAccountCallback() {
        UserApiClient.instance.loginWithKakaoAccount(
            context = binding.root.context,
            callback = appLoginCallback,
            serviceTerms = serviceTermsList
        )
    }

    // 카카오톡 앱 로그인 callback 구성
    private fun setAppLoginCallback() {
        appLoginCallback = { token, error ->
            if (error != null) {
                Timber.tag("signIn").e(error, "카카오톡으로 로그인 실패")

                // 뒤로가기 경우 예외 처리
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Timber.tag("signIn").e(error, "유저가 로그인 취소")
                } else {
                    // 카카오톡 연결 실패 시, 계정으로 로그인 시도
                    loginWithAccountCallback()
                }

            } else if (token != null) {
                postKakaoAccessToken(token)
                startSocialSyncActivity()
            }
        }
    }

    private fun loginWithAppCallback() {
        UserApiClient.instance.loginWithKakaoTalk(
            context = binding.root.context,
            callback = appLoginCallback,
            serviceTerms = serviceTermsList
        )
    }

    // 카카오톡 앱 유무에 따라 로그인 진행
    // TODO : Redirect URI 설정하고 나서 작동 확인하기
    private fun startKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(binding.root.context)) {
            loginWithAppCallback()
        } else {
            loginWithAccountCallback()
        }
    }

    private fun startSocialSyncActivity() {
        val intent = Intent(this, SocialSyncActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    // TODO : 유저 추가 정보 받아오기 - 사용 여부 기획 측과 논의 중
    private fun getUserInfo() {
        UserApiClient.instance.me { user, error ->
            var userNickname: String = user?.kakaoAccount?.profile?.nickname.toString()
        }
    }

    // TODO : 카카오의 Access Token 보내는 서버통신 구현
    // TODO : 서버통신 후 토큰 저장
    private fun postKakaoAccessToken(token: OAuthToken?) {
        var accessToken = token?.accessToken
        var refreshToken = token?.refreshToken
    }
}