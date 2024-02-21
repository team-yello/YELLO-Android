package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialCBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.extension.setOnSingleClickListener
import org.json.JSONObject

class TutorialCActivity : BindingActivity<ActivityTutorialCBinding>(R.layout.activity_tutorial_c) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        amplitudeCTutorial()
        setClickListener()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(NONE_ANIMATION, NONE_ANIMATION)
    }

    private fun setClickListener() {
        binding.root.setOnSingleClickListener {
            val isCodeTextEmpty = intent.getBooleanExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, false)
            val isFromOnBoarding =
                intent.getBooleanExtra(TutorialAActivity.EXTRA_FROM_ONBOARDING, false)

            val intent = Intent(this@TutorialCActivity, TutorialDActivity::class.java).apply {
                putExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, isCodeTextEmpty)
                putExtra(TutorialAActivity.EXTRA_FROM_ONBOARDING, isFromOnBoarding)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun amplitudeCTutorial() {
        AmplitudeUtils.trackEventWithProperties(
            EVENT_VIEW_ONBOARDING_TUTORIAL,
            JSONObject().put(NAME_TUTORIAL_STEP, VALUE_TUTORIAL_THREE),
        )
    }

    companion object {
        private const val NONE_ANIMATION = 0
        private const val EVENT_VIEW_ONBOARDING_TUTORIAL = "view_onboarding_tutorial"
        private const val NAME_TUTORIAL_STEP = "tutorial_step"
        private const val VALUE_TUTORIAL_THREE = "3"
    }
}
