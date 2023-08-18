package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialCBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialCActivity : BindingActivity<ActivityTutorialCBinding>(R.layout.activity_tutorial_c) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.setOnSingleClickListener {
            val iscodeEmpty = intent.getBooleanExtra("codeTextEmpty", false)
            val isFromOnBoarding = intent.getBooleanExtra("isFromOnBoarding", false)
            val intent = Intent(this@TutorialCActivity, TutorialDActivity::class.java).apply {
                putExtra("codeTextEmpty", iscodeEmpty)
                putExtra("isFromOnBoarding", isFromOnBoarding)
            }
            startActivity(intent)
            finish()
        }
    }
}
