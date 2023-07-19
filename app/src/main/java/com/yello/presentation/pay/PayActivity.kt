package com.yello.presentation.pay

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ActivityPayBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PayActivity : BindingActivity<ActivityPayBinding>(R.layout.activity_pay) {
    private val viewModel by viewModels<PayViewModel>()
    private lateinit var payAdapter: PayAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        observe()
    }

    private fun initView() {
        payAdapter = PayAdapter()
        binding.vpBanner.adapter = payAdapter
        binding.dotIndicator.setViewPager(binding.vpBanner)

        binding.tvOriginalPrice.paintFlags = binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun initEvent() {
        binding.clSubscribe.setOnSingleClickListener {
            viewModel.payCheck(0)
        }

        binding.clNameCheckOne.setOnSingleClickListener {
            viewModel.payCheck(1)
        }

        binding.clNameCheckTwo.setOnSingleClickListener {
            viewModel.payCheck(2)
        }

        binding.clNameCheckThree.setOnSingleClickListener {
            viewModel.payCheck(3)
        }

        binding.ivBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun observe() {
        viewModel.payCheck.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        Intent(this, PayEndActivity::class.java).apply {
                            payEndActivityLauncher.launch(this)
                        }
                    }
                    is UiState.Failure -> {
                        Intent(this, PayEndActivity::class.java).apply {
                            payEndActivityLauncher.launch(this)
                        }
                    }
                    else -> {}
                }

            }.launchIn(lifecycleScope)
    }

    private val payEndActivityLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }
}