package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialEndPluspointBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialEndPlusActivity :
    BindingActivity<ActivityTutorialEndPluspointBinding>(R.layout.activity_tutorial_end_pluspoint) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnEndTutorial.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_onboarding_yellostart")
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
    }
}
