package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialDBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.AmplitudeManager
import com.example.ui.base.BindingActivity
import com.example.ui.extension.setOnSingleClickListener
import org.json.JSONObject

class TutorialDActivity : BindingActivity<ActivityTutorialDBinding>(R.layout.activity_tutorial_d) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setClickListener()
        amplitudeDTutorial()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(NONE_ANIMATION, NONE_ANIMATION)
    }

    private fun setClickListener() {
        val isCodeTextEmpty = intent.getBooleanExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, false)
        val isFromOnBoarding = intent.getBooleanExtra(TutorialAActivity.EXTRA_FROM_ONBOARDING, false)
        binding.root.setOnSingleClickListener {
            if (isFromOnBoarding) {
                if (isCodeTextEmpty) {
                    val intent = Intent(this@TutorialDActivity, TutorialEndActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@TutorialDActivity, TutorialEndPlusActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(this)
                }
            }
            finish()
        }
    }

    private fun amplitudeDTutorial() {
        AmplitudeManager.trackEventWithProperties(
            EVENT_VIEW_ONBOARDING_TUTORIAL,
            JSONObject().put(NAME_TUTORIAL_STEP, VALUE_TUTORIAL_FOUR),
        )
    }

    companion object {
        private const val NONE_ANIMATION = 0
        private const val EVENT_VIEW_ONBOARDING_TUTORIAL = "view_onboarding_tutorial"
        private const val NAME_TUTORIAL_STEP = "tutorial_step"
        private const val VALUE_TUTORIAL_FOUR = "4"
    }
}
