package com.yello.presentation.auth

import android.content.Intent
import android.content.Intent.EXTRA_EMAIL
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.talk.TalkApiClient
import com.yello.R
import com.yello.databinding.ActivitySocialSyncBinding
import com.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.yello.presentation.onboarding.activity.OnBoardingActivity
import com.yello.util.context.yelloSnackbar
import timber.log.Timber

class SocialSyncActivity :
    BindingActivity<ActivitySocialSyncBinding>(R.layout.activity_social_sync) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSocialSyncButtonListener()
    }

    private fun initSocialSyncButtonListener() {
        binding.btnSocialSync.setOnSingleClickListener {
            getFriendsList()
        }
    }

    // 친구목록 동의 받고 온보딩 뷰로 이동
    private fun getFriendsList() {
        TalkApiClient.instance.friends { friends, error ->
            if (error == null) {
                startOnBoardingActivity()
            } else {
                yelloSnackbar(binding.root, getString(R.string.msg_error))
                Timber.tag(TAG_SYNC).e(error, getString(R.string.social_sync_error_kakao_profile))
            }
        }
    }

    private fun startOnBoardingActivity() {
        intent.apply {
            val kakaoId = getLongExtra(EXTRA_KAKAO_ID, -1)
            val email = getStringExtra(EXTRA_EMAIL)
            val profileImage = getStringExtra(EXTRA_PROFILE_IMAGE)
            Timber.d("KAKAO ID : $kakaoId, EMAIL : $email, PROFILE : $profileImage")
            Intent(this@SocialSyncActivity, OnBoardingActivity::class.java).apply {
                putExtra(EXTRA_KAKAO_ID, kakaoId)
                putExtra(EXTRA_EMAIL, email)
                putExtra(EXTRA_PROFILE_IMAGE, profileImage)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
            finish()
        }
    }

    private companion object {
        const val TAG_SYNC = "authSocialSync"
    }
}
