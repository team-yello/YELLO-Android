package com.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.yello.R
import com.yello.databinding.ActivitySocialSyncBinding
import com.yello.presentation.main.MainActivity
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
            startOnBoardingActivity()
        }
    }

    private fun getFriendsList() {
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Timber.tag(TAG_SYNC).e(error, "카카오 프로필 가져오기 실패")
            } else if (friends != null) {
                val friendList: List<Friend>? = friends.elements
                val friendIdList = friendList?.map { friend -> friend.id }
            } else {
                Timber.tag(TAG_SYNC).d("연동 가능한 카카오톡 친구 없음")
            }
        }
    }

    // TODO : 온보딩 뷰로 이동시키기
    private fun startOnBoardingActivity() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    private companion object {
        const val TAG_SYNC = "authSocialSync"
    }
}