package com.yello.presentation.onboarding.activity

import android.content.Intent
import android.content.Intent.EXTRA_EMAIL
import android.os.Bundle
import androidx.activity.viewModels
import com.example.ui.base.BindingActivity
import com.yello.R
import com.yello.databinding.ActivityOnboardingBinding
import com.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.yello.presentation.auth.SocialSyncActivity
import com.yello.presentation.onboarding.ViewPagerFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onBackPressed() {
        if (binding.vpOnboarding.currentItem == 6) return
        if (binding.vpOnboarding.currentItem == 0) {
            val intent = Intent(this, SocialSyncActivity::class.java)
            startActivity(intent)
            return
        }
        viewModel.navigateToBackPage()
    }
}
