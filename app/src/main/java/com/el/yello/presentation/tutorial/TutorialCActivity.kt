package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialCBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class TutorialCActivity : BindingActivity<ActivityTutorialCBinding>(R.layout.activity_tutorial_c) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AmplitudeUtils.trackEventWithProperties(
            "view_onboarding_tutorial",
            JSONObject().put("tutorial_step", "3"),
        )
        setClickListener()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    private fun setClickListener() {
        binding.root.setOnSingleClickListener {
            val isCodeTextEmpty = intent.getBooleanExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, false)
            val isFromOnBoarding = intent.getBooleanExtra(TutorialAActivity.EXTRA_FROM_ONBOARDING, false)

            val intent = Intent(this@TutorialCActivity, TutorialDActivity::class.java).apply {
                putExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, isCodeTextEmpty)
                putExtra(TutorialAActivity.EXTRA_FROM_ONBOARDING, isFromOnBoarding)
            }
            startActivity(intent)
            finish()
        }
    }
}
