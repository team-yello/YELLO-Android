package com.el.yello.presentation.pay

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.android.billingclient.api.queryProductDetails
import com.el.yello.R
import com.el.yello.databinding.ActivityPayBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

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

        binding.tvOriginalPrice.paintFlags =
            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
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
        viewModel.payCheck.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {

                }

                is UiState.Failure -> {

                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }
}
