package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialDBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class TutorialDActivity : BindingActivity<ActivityTutorialDBinding>(R.layout.activity_tutorial_d) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AmplitudeUtils.trackEventWithProperties(
            "view_onboarding_tutorial",
            JSONObject().put("tutorial_step", "4"),
        )
        setClickListener()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    private fun setClickListener() {
        val isCodeTextEmpty = intent.getBooleanExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, false)
        val isFromOnBoarding = intent.getBooleanExtra(TutorialAActivity.EXTRA_FROM_ONBOARDING, false)
        binding.root.setOnSingleClickListener {
            Log.d("minju",isCodeTextEmpty.toString())
            if (isFromOnBoarding) {
                if (isCodeTextEmpty) {
                    Log.d("minju2",isCodeTextEmpty.toString())
                    val intent = Intent(this@TutorialDActivity, TutorialEndActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("minju3",isCodeTextEmpty.toString())
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
}
