package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialEndPointBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialEndActivity :
    BindingActivity<ActivityTutorialEndPointBinding>(R.layout.activity_tutorial_end_point) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnEndTutorial.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_ONBOARDING_YELLO_START)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(NONE_ANIMATION, NONE_ANIMATION)
    }

    companion object {
        private const val NONE_ANIMATION = 0
        private const val EVENT_CLICK_ONBOARDING_YELLO_START = "click_onboarding_yellostart"
    }
}
