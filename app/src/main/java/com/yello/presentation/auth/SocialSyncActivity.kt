package com.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.talk.TalkApiClient
import com.yello.R
import com.yello.databinding.ActivitySocialSyncBinding
import com.yello.presentation.onboarding.activity.OnBoardingActivity
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
                Timber.tag(TAG_SYNC).e(error, getString(R.string.social_sync_error_kakao_profile))
            }
        }
    }

    private fun startOnBoardingActivity() {
        Intent(this, OnBoardingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    private companion object {
        const val TAG_SYNC = "authSocialSync"
    }
}