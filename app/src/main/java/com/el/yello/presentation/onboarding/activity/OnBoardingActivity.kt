package com.el.yello.presentation.onboarding.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.el.yello.R
import com.el.yello.databinding.ActivityOnboardingBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_EMAIL
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_GENDER
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_NAME
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.el.yello.presentation.auth.SocialSyncActivity
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel by viewModels<OnBoardingViewModel>()
    private var backPressedTime: Long = 0
    private val BACK_PRESSED_INTERVAL = 2000 // 2초
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentExtraData()
    }

    // 뒤로 가기
    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_main_fragment)
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId == R.id.universityInfoFragment) {
            val intent = Intent(this, SocialSyncActivity::class.java)
            startActivity(intent)
        } else if (currentDestinationId == R.id.codeFragment) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime < BACK_PRESSED_INTERVAL) {
                finish() // 두 번째 뒤로 가기 누름으로 앱 종료
            } else {
                backPressedTime = currentTime
                Toast.makeText(this, "버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
            }
        } else if (currentDestinationId == R.id.addFriendFragment) {
            if (viewModel.friendListState.value == UiState.Loading) {
            }
        } else {
            super.onBackPressed()
            progressBarMinus()
        }
    }

    // backbtn 뒤로 가기 클릭 시
    fun onBackButtonClicked(view: View?) {
        val navController = findNavController(R.id.nav_main_fragment)
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId == R.id.universityInfoFragment) {
            val intent = Intent(this, SocialSyncActivity::class.java)
            startActivity(intent)
        } else if (currentDestinationId == R.id.addFriendFragment) {
            if (viewModel.friendListState.value == UiState.Loading) {
            }
        } else {
            navController.popBackStack()
            progressBarMinus()
            Log.e("minju", navController.currentDestination.toString())
        }
    }

    private fun getIntentExtraData() {
        intent.apply {
            viewModel.kakaoId = getLongExtra(EXTRA_KAKAO_ID, -1).toString()
            viewModel.email = getStringExtra(EXTRA_EMAIL) ?: ""
            viewModel.profileImg = getStringExtra(EXTRA_PROFILE_IMAGE) ?: ""
            viewModel.name = getStringExtra(EXTRA_NAME) ?: ""
            viewModel.gender = getStringExtra(EXTRA_GENDER) ?: ""
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

    fun showBackbtn() {
        binding.backBtn.visibility = View.VISIBLE
    }

    fun endTutorialActivity() {
        val intent = TutorialAActivity.newIntent(this, true)
        intent.putExtra("codeTextEmpty", viewModel.isCodeTextEmpty())
        startActivity(intent)
        finish()
    }
}
