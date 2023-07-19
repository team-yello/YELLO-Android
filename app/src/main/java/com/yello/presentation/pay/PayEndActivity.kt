package com.yello.presentation.pay

import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ActivityPayEndBinding

class PayEndActivity: BindingActivity<ActivityPayEndBinding>(R.layout.activity_pay_end) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEvent()
    }

    private fun initEvent() {
        binding.ivBack.setOnSingleClickListener {
            finish()
        }
        binding.tvExit.setOnSingleClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}