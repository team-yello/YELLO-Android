package com.el.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivitySocialSyncBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_EMAIL
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_GENDER
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_NAME
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialSyncActivity :
    BindingActivity<ActivitySocialSyncBinding>(R.layout.activity_social_sync) {

    private val viewModel by viewModels<SocialSyncViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSocialSyncBtnListener()
        observeFriendsAccessState()
    }

    private fun initSocialSyncBtnListener() {
        binding.btnSocialSync.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_onboarding_kakao_friends")
            viewModel.getKakaoFriendsList()
        }
    }

    // 친구목록 동의만 받고 온보딩 뷰로 이동 (친구 목록은 이후에 추가)
    private fun observeFriendsAccessState() {
        viewModel.getFriendListState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    startOnBoardingActivity()
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    private fun startOnBoardingActivity() {
        Intent(this@SocialSyncActivity, OnBoardingActivity::class.java).apply {
            putExtra(EXTRA_KAKAO_ID, getLongExtra(EXTRA_KAKAO_ID, -1))
            putExtra(EXTRA_EMAIL, getStringExtra(EXTRA_EMAIL))
            putExtra(EXTRA_PROFILE_IMAGE, getStringExtra(EXTRA_PROFILE_IMAGE))
            putExtra(EXTRA_NAME, getStringExtra(EXTRA_NAME))
            putExtra(EXTRA_GENDER, getStringExtra(EXTRA_GENDER))
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }
}
