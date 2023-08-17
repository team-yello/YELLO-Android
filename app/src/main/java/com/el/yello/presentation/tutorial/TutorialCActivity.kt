package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialCBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialCActivity : BindingActivity<ActivityTutorialCBinding>(R.layout.activity_tutorial_c) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.setOnSingleClickListener {
            val c = intent.getBooleanExtra("codeTextEmpty", false)
            val intent = Intent(this@TutorialCActivity, TutorialDActivity::class.java).apply {
                putExtra("codeTextEmpty", c)
            }
            startActivity(intent)
            finish()
        }
    }
}
