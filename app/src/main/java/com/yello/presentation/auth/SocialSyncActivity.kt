package com.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
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

    // TODO: 친구 아이디 리스트 저장
    private fun getFriendsList() {
        TalkApiClient.instance.friends { friends, error ->
            if (error == null) {
                val friendList: List<Friend> = friends?.elements ?: listOf<Friend>()
                val friendIdList = friendList.map { friend -> friend.id }
                toast("카카오톡 친구 연동 완료!")
                startOnBoardingActivity()
            } else {
                Timber.tag(TAG_SYNC).e(error, "카카오 프로필 가져오기 실패")
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