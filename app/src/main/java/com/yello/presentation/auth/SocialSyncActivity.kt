package com.yello.presentation.auth

import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.yello.R
import com.yello.databinding.ActivitySocialSyncBinding
import timber.log.Timber

class SocialSyncActivity :
    BindingActivity<ActivitySocialSyncBinding>(R.layout.activity_social_sync) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnSignIn.setOnSingleClickListener {
            getFriendsList()

            // TODO : 온보딩 뷰로 이동시키기
        }
    }

    // TODO : 서버 측에 어떤 친구목록 형식 보내줘야하는지 확인하고 저장시키기
    private fun getFriendsList() {
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Timber.tag("signIn").e(error, "카카오 프로필 가져오기 실패")
            } else if (friends != null) {
                val friendList: List<Friend>? = friends.elements
                val friendIdList = friendList?.map { friend -> friend.id }
            } else {
                Timber.tag("signIn").e("연동 가능한 카카오톡 친구 없음")
            }
        }
    }
}