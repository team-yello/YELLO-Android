package com.el.yello.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialDBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialDActivity : BindingActivity<ActivityTutorialDBinding>(R.layout.activity_tutorial_d) {
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialDBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnSingleClickListener {
            val codeTextValue = intent.getStringExtra("codeText")
            if (codeTextValue.isNullOrEmpty()) {
                val intent = Intent(this, TutorialEndActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, TutorialEndPlusActivity::class.java)
                startActivity(intent)
            }
        }
    }
}





