package com.yello.presentation.auth

import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.yello.R
import com.yello.databinding.ActivitySocialSyncBinding
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import timber.log.Timber

class SocialSyncActivity : BindingActivity<ActivitySocialSyncBinding>(R.layout.activity_social_sync) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Timber.tag("signIn").e(error, "카카오 프로필 가져오기 실패")
            }
            else if (friends != null) {
                val friendList : List<Friend>? = friends.elements
            }
        }
    }
}