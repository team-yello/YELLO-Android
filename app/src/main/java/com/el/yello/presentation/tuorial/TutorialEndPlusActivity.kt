package com.el.yello.presentation.tuorial

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityTutorialEndPointBinding
import com.el.yello.presentation.main.MainActivity
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class TutorialEndPlusActivity :
    BindingActivity<ActivityTutorialEndPointBinding>(R.layout.activity_tutorial_end_pluspoint) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnEndTutorial.setOnSingleClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
