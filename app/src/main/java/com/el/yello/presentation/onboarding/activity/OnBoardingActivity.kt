package com.el.yello.presentation.onboarding.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.el.yello.R
import com.el.yello.databinding.ActivityOnboardingBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_EMAIL
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_GENDER
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_NAME
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    private val viewModel by viewModels<OnBoardingViewModel>()

    private var backPressedTime: Long = 0

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val navController = findNavController(R.id.nav_main_fragment)
            when (navController.currentDestination?.id) {
                R.id.selectStudentFragment -> {
                    if (System.currentTimeMillis() - backPressedTime >= BACK_PRESSED_INTERVAL) {
                        backPressedTime = System.currentTimeMillis()
                        toast(getString(R.string.main_toast_back_pressed))
                    } else {
                        finish()
                    }
                }

                R.id.codeFragment -> {
                    if (System.currentTimeMillis() - backPressedTime >= BACK_PRESSED_INTERVAL) {
                        backPressedTime = System.currentTimeMillis()
                        toast(getString(R.string.main_toast_back_pressed))
                    } else {
                        finish()
                    }
                }

                else -> {
                    navController.popBackStack()
                    progressBarMinus()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentExtraData()
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    fun onBackButtonClicked(view: View) {
        val navController = findNavController(R.id.nav_main_fragment)
        when (navController.currentDestination?.id) {
            R.id.selectStudentFragment -> {
            }

            else -> {
                navController.popBackStack()
                progressBarMinus()
            }
        }
    }

    private fun getIntentExtraData() {
        val bundle: Bundle? = intent.extras
        intent.apply {
            if (bundle != null) {
                viewModel.kakaoId = bundle.getLong(EXTRA_KAKAO_ID, 0).toString()
                viewModel.nameText.value = bundle.getString(EXTRA_NAME, "")
                viewModel.gender = bundle.getString(EXTRA_GENDER, "")
                viewModel.email = bundle.getString(EXTRA_EMAIL, "")
                viewModel.profileImg = bundle.getString(EXTRA_PROFILE_IMAGE, "")
            }
        }
    }

    fun endTutorialActivity() {
        val intent = GetAlarmActivity.newIntent(this, true)
        intent.putExtra(EXTRA_CODE_TEXT_EMPTY, viewModel.isCodeTextEmpty())
        startActivity(intent)
        finish()
    }

    fun progressBarPlus() {
        viewModel.plusCurrentPercent()
        val animator = ObjectAnimator.ofInt(
            binding.onboardingProgressbar,
            PROPERTY_PROGRESS,
            binding.onboardingProgressbar.progress,
            viewModel.currentPercent,
        )
        animator.duration = 300
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    fun progressBarMinus() {
        viewModel.minusCurrentPercent()
        val animator = ObjectAnimator.ofInt(
            binding.onboardingProgressbar,
            PROPERTY_PROGRESS,
            binding.onboardingProgressbar.progress,
            viewModel.currentPercent,
        )
        animator.duration = 300
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    fun hideBackBtn() {
        binding.backBtn.visibility = View.INVISIBLE
    }

    fun showBackBtn() {
        binding.backBtn.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(NONE_ANIMATION, NONE_ANIMATION)
    }

    companion object {
        private const val BACK_PRESSED_INTERVAL = 2000
        private const val NONE_ANIMATION = 0
        const val EXTRA_CODE_TEXT_EMPTY = "codeTextEmpty"
        const val PROPERTY_PROGRESS = "progress"
    }
}
