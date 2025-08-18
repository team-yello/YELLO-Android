package com.el.yello.presentation.onboarding.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.el.yello.presentation.onboarding.screen.EditNameRoute
import com.example.ui.compose.theme.YelloTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNameActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: OnBoardingActivity Compose 전환 후 EditNameActivity -> OnBoardingActivity 통합 예정
        // 현재: EditNameActivity → OnBoardingActivity (별도 Activity 2개)
        // 변경: OnBoardingActivity 내부에서 EditName부터 전체 온보딩 플로우 처리 (EditNameActivity 삭제)

        setContent {
            YelloTheme {
                EditNameRoute(
                    kakaoId = intent.getLongExtra(EXTRA_KAKAO_ID, 0L),
                    userName = intent.getStringExtra(EXTRA_NAME) ?: "",
                    profileImageUrl = intent.getStringExtra(EXTRA_PROFILE_IMAGE) ?: "",
                    email = intent.getStringExtra(EXTRA_KAKAO_EMAIL) ?: "",
                    gender = intent.getStringExtra(EXTRA_GENDER) ?: "",
                    navigateToOnBoarding = { kakaoId, name, profileImage, email, gender ->
                        val intent = Intent(this@EditNameActivity, OnBoardingActivity::class.java).apply {
                            putExtra(EXTRA_KAKAO_ID, kakaoId)
                            putExtra(EXTRA_NAME, name)
                            putExtra(EXTRA_PROFILE_IMAGE, profileImage)
                            putExtra(EXTRA_KAKAO_EMAIL, email)
                            putExtra(EXTRA_GENDER, gender)
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }
                        startActivity(intent)
                        finish()
                    },
                    navigateBack = { finish() }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_KAKAO_ID = "KAKAO_ID"
        private const val EXTRA_NAME = "NAME"
        private const val EXTRA_GENDER = "GENDER"
        private const val EXTRA_KAKAO_EMAIL = "KAKAO_EMAIL"
        private const val EXTRA_PROFILE_IMAGE = "PROFILE_IMAGE"
    }
}
