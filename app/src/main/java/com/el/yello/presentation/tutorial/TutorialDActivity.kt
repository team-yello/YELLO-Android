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
        setContentView(binding.root)

        binding.root.setOnSingleClickListener {
            val intent = Intent(this, TutorialEndActivity::class.java)
            startActivity(intent)
        }
    }
}
