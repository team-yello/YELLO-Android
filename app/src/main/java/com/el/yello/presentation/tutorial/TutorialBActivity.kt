package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialBBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialBActivity : BindingActivity<ActivityTutorialBBinding>(R.layout.activity_tutorial_b) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.setOnSingleClickListener {
            val b = intent.getBooleanExtra("codeTextEmpty", false)
            val intent = Intent(this@TutorialBActivity, TutorialCActivity::class.java).apply {
                putExtra("codeTextEmpty", b)
            }
            startActivity(intent)
            finish()
        }
    }
}
