package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialDBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class TutorialDActivity : BindingActivity<ActivityTutorialDBinding>(R.layout.activity_tutorial_d) {

    override fun onCreate(savedInstanceState: Bundle?) {
        AmplitudeUtils.trackEventWithProperties(
            "view_onboarding_tutorial",
            JSONObject().put("tutorial_step", "4"),
        )
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val iscodeEmpty = intent.getBooleanExtra("codeTextEmpty", false)
        val isFromOnBoarding = intent.getBooleanExtra("isFromOnBoarding", false)
        binding.root.setOnSingleClickListener {
            if (isFromOnBoarding) {
                if (iscodeEmpty) {
                    val intent = Intent(this@TutorialDActivity, TutorialEndActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@TutorialDActivity, TutorialEndPlusActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(this)
                }
                finish()
            }
        }
    }
}
