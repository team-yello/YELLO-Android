package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialBBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity.Companion.EXTRA_CODE_TEXT_EMPTY
import com.el.yello.presentation.tutorial.TutorialAActivity.Companion.EXTRA_FROM_ONBOARDING
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class TutorialBActivity : BindingActivity<ActivityTutorialBBinding>(R.layout.activity_tutorial_b) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AmplitudeUtils.trackEventWithProperties(
            "view_onboarding_tutorial",
            JSONObject().put("tutorial_step", "2"),
        )
        setClickListener()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
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
}
