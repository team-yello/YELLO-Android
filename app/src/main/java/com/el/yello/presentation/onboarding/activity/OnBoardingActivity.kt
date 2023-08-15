package com.el.yello.presentation.onboarding.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.el.yello.R
import com.el.yello.databinding.ActivityOnboardingBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_EMAIL
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import kotlin.concurrent.timer

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel by viewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 14
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentExtraData()
    }

    fun onBackButtonClicked(view: View?) {
        val navController = findNavController(R.id.nav_main_fragment)
        navController.popBackStack()
        progressBarMinus()
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
        val currentPercent = viewModel.currentpercent
        val plusPercent = 14
        binding.onboardingProgressbar.progress = currentPercent
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 8, initialDelay = 300) {
            if (deltaTime > currentPercent + plusPercent) cancel()
            binding.onboardingProgressbar.setProgress(++deltaTime)
            println(binding.onboardingProgressbar.progress)
        }
    }

    fun progressBarMinus() {
        viewModel.minusCurrentPercent()
        val currentPercent = viewModel.currentpercent
        val minusPercent = 14
        binding.onboardingProgressbar.progress = currentPercent
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 8, initialDelay = 300) {
            if (deltaTime > currentPercent - minusPercent) cancel()
            binding.onboardingProgressbar.setProgress(++deltaTime)
            println(binding.onboardingProgressbar.progress)
        }
    }
}
