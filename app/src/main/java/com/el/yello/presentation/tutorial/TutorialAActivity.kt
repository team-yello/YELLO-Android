package com.el.yello.presentation.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialABinding
import com.example.ui.base.BindingActivity
import com.example.ui.intent.boolExtra
import com.example.ui.view.setOnSingleClickListener

class TutorialAActivity : BindingActivity<ActivityTutorialABinding>(R.layout.activity_tutorial_a) {
    private val isFromOnBoarding by boolExtra()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
