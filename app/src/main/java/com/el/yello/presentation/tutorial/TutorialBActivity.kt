package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialBBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialBActivity : BindingActivity<ActivityTutorialBBinding>(R.layout.activity_tutorial_b) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.setOnSingleClickListener {
            val isCodeEmpty = intent.getBooleanExtra("codeTextEmpty", false)
            val isFromOnBoarding = intent.getBooleanExtra("isFromOnBoarding", false)
            val intent = Intent(this@TutorialBActivity, TutorialCActivity::class.java).apply {
                putExtra("codeTextEmpty", isCodeEmpty)
                putExtra("isFromOnBoarding", isFromOnBoarding)
            }
            startActivity(intent)
            finish()
        }
    }
}
