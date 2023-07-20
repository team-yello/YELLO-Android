package com.el.yello.presentation.auth

import android.content.Intent
import android.content.Intent.EXTRA_EMAIL
import android.net.Uri
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivitySocialSyncBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.talk.TalkApiClient
import timber.log.Timber

class SocialSyncActivity :
    BindingActivity<ActivitySocialSyncBinding>(R.layout.activity_social_sync) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSocialSyncButtonListener()
        initinfoButton()
    }

    private fun initSocialSyncButtonListener() {
        binding.btnSocialSync.setOnSingleClickListener {
            getFriendsList()
        }
    }

    private fun initinfoButton() {
        binding.tvSocialSyncInfo.setOnSingleClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://yell0.notion.site/2afc2a1e60774dfdb47c4d459f01b1d9")))
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
