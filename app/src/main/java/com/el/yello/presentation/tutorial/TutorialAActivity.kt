package com.el.yello.presentation.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialABinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity.Companion.EXTRA_CODE_TEXT_EMPTY
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.intent.boolExtra
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class TutorialAActivity : BindingActivity<ActivityTutorialABinding>(R.layout.activity_tutorial_a) {

    private val isFromOnBoarding by boolExtra()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AmplitudeUtils.trackEventWithProperties(
            "view_onboarding_tutorial",
            JSONObject().put("tutorial_step", "1"),
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
            val intent = Intent(this@TutorialAActivity, TutorialBActivity::class.java).apply {
                putExtra(EXTRA_CODE_TEXT_EMPTY, isCodeTextEmpty)
                putExtra(EXTRA_FROM_ONBOARDING, isFromOnBoarding)
            }
            startActivity(intent)
            finish()
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context, isFromOnBoarding: Boolean) =
            Intent(context, TutorialAActivity::class.java).apply {
                putExtra("isFromOnBoarding", isFromOnBoarding)
            }

        const val EXTRA_FROM_ONBOARDING = "isFromOnBoarding"
    }
}
