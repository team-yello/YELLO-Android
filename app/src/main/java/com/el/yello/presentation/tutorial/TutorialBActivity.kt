package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialBBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity.Companion.EXTRA_CODE_TEXT_EMPTY
import com.el.yello.presentation.tutorial.TutorialAActivity.Companion.EXTRA_FROM_ONBOARDING
import com.el.yello.util.AmplitudeManager
import com.example.ui.base.BindingActivity
import com.example.ui.extension.setOnSingleClickListener
import org.json.JSONObject

class TutorialBActivity : BindingActivity<ActivityTutorialBBinding>(R.layout.activity_tutorial_b) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amplitudeBTutorial()
        setClickListener()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(NONE_ANIMATION, NONE_ANIMATION)
    }

    private fun setClickListener() {
        binding.root.setOnSingleClickListener {
            val isCodeTextEmpty = intent.getBooleanExtra(EXTRA_CODE_TEXT_EMPTY, false)
            val isFromOnBoarding = intent.getBooleanExtra(EXTRA_FROM_ONBOARDING, false)

            val intent = Intent(this@TutorialBActivity, TutorialCActivity::class.java).apply {
                putExtra(EXTRA_CODE_TEXT_EMPTY, isCodeTextEmpty)
                putExtra(EXTRA_FROM_ONBOARDING, isFromOnBoarding)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun amplitudeBTutorial() {
        AmplitudeManager.trackEventWithProperties(
            EVENT_VIEW_ONBOARDING_TUTORIAL,
            JSONObject().put(NAME_TUTORIAL_STEP, VALUE_TUTORIAL_TWO),
        )
    }

    companion object {
        private const val NONE_ANIMATION = 0
        private const val EVENT_VIEW_ONBOARDING_TUTORIAL = "view_onboarding_tutorial"
        private const val NAME_TUTORIAL_STEP = "tutorial_step"
        private const val VALUE_TUTORIAL_TWO = "2"
    }
}
