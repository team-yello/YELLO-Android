package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialEndPointBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.base.BindingActivity
import com.example.ui.extension.setOnSingleClickListener

class TutorialEndActivity :
    BindingActivity<ActivityTutorialEndPointBinding>(R.layout.activity_tutorial_end_point) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEndClickListener()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(NONE_ANIMATION, NONE_ANIMATION)
    }
    private fun initEndClickListener() {
        binding.btnEndTutorial.setOnSingleClickListener {
            AmplitudeManager.trackEventWithProperties(EVENT_CLICK_ONBOARDING_YELLO_START)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private const val NONE_ANIMATION = 0
        private const val EVENT_CLICK_ONBOARDING_YELLO_START = "click_onboarding_yellostart"
    }
}
