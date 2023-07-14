package com.yello.presentation.onboarding.activity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.ui.base.BindingActivity
import com.yello.R
import com.yello.databinding.ActivityOnboardingBinding
import com.yello.presentation.onboarding.ViewPagerFragmentAdapter

class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel by viewModels<OnBoardingViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewPager()
        setupCurrentPage()
    }

    private fun initViewPager() {
        val viewPager = binding.vpOnboarding
        val viewPagerAdapter = ViewPagerFragmentAdapter(this)
        viewPager.adapter = viewPagerAdapter
    }

    private fun setupCurrentPage() {
        viewModel.currentPage.observe(this) { page ->
            binding.vpOnboarding.currentItem = page
        }
    }
}
