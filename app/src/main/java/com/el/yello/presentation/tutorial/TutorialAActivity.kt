package com.el.yello.presentation.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialABinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.intent.boolExtra
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class TutorialAActivity : BindingActivity<ActivityTutorialABinding>(R.layout.activity_tutorial_a) {
    private val isFromOnBoarding by boolExtra()
    // private val viewModel by viewModels<TutorialViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AmplitudeUtils.trackEventWithProperties(
            "view_onboarding_tutorial",
            JSONObject().put("tutorial_step", "1"),
        )
        // viewModel.currentView = 1
        binding.root.setOnSingleClickListener {
            val isCodeEmpty = intent.getBooleanExtra("codeTextEmpty", false)
            val intent = Intent(this@TutorialAActivity, TutorialBActivity::class.java).apply {
                putExtra("codeTextEmpty", isCodeEmpty)
                putExtra("isFromOnBoarding", isFromOnBoarding)
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
    }
}
