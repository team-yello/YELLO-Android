package com.el.yello.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivitySocialSyncBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_EMAIL
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SocialSyncActivity :
    BindingActivity<ActivitySocialSyncBinding>(R.layout.activity_social_sync) {

    private val viewModel by viewModels<SocialSyncViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSocialSyncBtnListener()
        initInfoBtnListener()
        observeKakaoFriendsList()
    }

    private fun initSocialSyncBtnListener() {
        binding.btnSocialSync.setOnSingleClickListener {
            viewModel.getKakaoFriendsList()
        }
    }

    // 개인정보처리방침 버튼의 링크 이동 리스너
    private fun initInfoBtnListener() {
        binding.tvSocialSyncInfo.setOnSingleClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PERSONAL_INFO_URL)))
        }
    }

    // 친구목록 동의만 받고 온보딩 뷰로 이동 (친구 목록은 이후에 추가)
    private fun observeKakaoFriendsList() {
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
        const val PERSONAL_INFO_URL = "https://yell0.notion.site/2afc2a1e60774dfdb47c4d459f01b1d9"
    }
}
