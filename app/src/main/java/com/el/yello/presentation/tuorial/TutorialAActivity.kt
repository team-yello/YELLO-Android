package com.el.yello.presentation.tuorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialABinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialAActivity : BindingActivity<ActivityTutorialABinding>(R.layout.activity_tutorial_a) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root.setOnSingleClickListener {
            val intent = Intent(this, TutorialBActivity::class.java)
            startActivity(intent)
        }
    }
}
