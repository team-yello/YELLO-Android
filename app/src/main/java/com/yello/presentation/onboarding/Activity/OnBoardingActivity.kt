package com.yello.presentation.onboarding.Activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.ui.base.BindingActivity
import com.yello.R
import com.yello.databinding.ActivityOnboardingBinding
import com.yello.presentation.onboarding.ViewPagerFragmentAdapter

class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        initViewPager()
    }

    private fun initViewPager() {
        val viewPager = binding.vpOnboarding
        val viewPagerAdapter = ViewPagerFragmentAdapter(this)
        viewPager.adapter = viewPagerAdapter
    }
}
