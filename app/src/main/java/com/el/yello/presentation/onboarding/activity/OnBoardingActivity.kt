package com.el.yello.presentation.onboarding.activity

import android.content.Intent
import android.content.Intent.EXTRA_EMAIL
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityOnboardingBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.el.yello.presentation.auth.SocialSyncActivity
import com.el.yello.presentation.onboarding.ViewPagerFragmentAdapter
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        getIntentExtraData()
        initViewPager()
        setupCurrentPage()
    }

    private fun getIntentExtraData() {
        intent.apply {
            viewModel.kakaoId = getLongExtra(EXTRA_KAKAO_ID, -1).toString()
            viewModel.email = getStringExtra(EXTRA_EMAIL) ?: ""
            viewModel.profileImg = getStringExtra(EXTRA_PROFILE_IMAGE) ?: ""
        }
    }

    // TODO : 고등학생, 대학생 분기 처리
    private fun initViewPager() {
        val viewPager = binding.vpOnboarding
        val viewPagerAdapter = ViewPagerFragmentAdapter(this)
        viewPager.adapter = viewPagerAdapter
        binding.vpOnboarding.run {
            isUserInputEnabled = false
        }
    }

    private fun setupCurrentPage() {
        viewModel.currentPage.observe(this) { page ->
            binding.vpOnboarding.currentItem = page
        }
    }

    // 내장된 백버튼 클릭 시 이전 화면으로 이동

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.vpOnboarding.currentItem == CURRENT_ITEM_LAST) return
            if (binding.vpOnboarding.currentItem == CURRENT_ITEM_START) {
                val intent = Intent(this@OnBoardingActivity, SocialSyncActivity::class.java)
                startActivity(intent)
                return
            }
            viewModel.navigateToBackPage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

    companion object {
        private const val CURRENT_ITEM_START = 0
        private const val CURRENT_ITEM_LAST = 7
    }
}
