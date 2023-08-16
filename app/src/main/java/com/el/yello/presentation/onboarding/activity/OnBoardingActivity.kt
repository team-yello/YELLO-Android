package com.el.yello.presentation.onboarding.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.el.yello.R
import com.el.yello.databinding.ActivityOnboardingBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_EMAIL
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.el.yello.presentation.auth.SocialSyncActivity
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentExtraData()
    }

    fun onBackButtonClicked(view: View?) {
        intent.putExtra("codeText", viewModel.codeText.value)
        startActivity(intent)
        val navController = findNavController(R.id.nav_main_fragment)
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId == R.id.universityInfoFragment) {
            val intent = Intent(this, SocialSyncActivity::class.java)
            startActivity(intent)
        } else {
            navController.popBackStack()
            progressBarMinus()
        }
    }

    private fun getIntentExtraData() {
        intent.apply {
            viewModel.kakaoId = getLongExtra(EXTRA_KAKAO_ID, -1).toString()
            viewModel.email = getStringExtra(EXTRA_EMAIL) ?: ""
            viewModel.profileImg = getStringExtra(EXTRA_PROFILE_IMAGE) ?: ""
        }
    }

    fun progressBarPlus() {
        viewModel.plusCurrentPercent()
        val animator = ObjectAnimator.ofInt(binding.onboardingProgressbar, "progress", binding.onboardingProgressbar.progress, viewModel.currentpercent)
        animator.duration = 300
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    fun progressBarMinus() {
        viewModel.minusCurrentPercent()
        val animator = ObjectAnimator.ofInt(binding.onboardingProgressbar, "progress", binding.onboardingProgressbar.progress, viewModel.currentpercent)
        animator.duration = 300
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    fun hideViews() {
        binding.backBtn.visibility = View.INVISIBLE
        binding.onboardingProgressbar.visibility = View.INVISIBLE
    }

    fun hideBackbtn() {
        binding.backBtn.visibility = View.INVISIBLE
    }
}
