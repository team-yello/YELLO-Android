package com.el.yello.presentation.pay

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.el.yello.R
import com.el.yello.databinding.ActivityPayBinding
import com.el.yello.presentation.util.BillingCallback
import com.el.yello.presentation.util.BillingManager
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PayActivity : BindingActivity<ActivityPayBinding>(R.layout.activity_pay) {
    private val viewModel by viewModels<PayViewModel>()
    private lateinit var payAdapter: PayAdapter

    private lateinit var manager: BillingManager

    private var productDetails = listOf<ProductDetails>()

    private var currentSubscription: Purchase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        observe()
        setBillingManager()
    }

    private fun setBillingManager() {
        manager = BillingManager(this, object : BillingCallback {
            override fun onBillingConnected() {
                manager.getProductDetails() { list ->
                    productDetails = list
                }

                manager.checkPurchased(billingType = "") {
                    currentSubscription = it
                }
            }

            override fun onSuccess(purchase: Purchase) {
                currentSubscription = purchase
            }

            override fun onFailure(responseCode: Int) {
                toast("구매 도중 오류가 발생하였습니다. (${responseCode})")
            }
        })
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
            productDetails.find { it.productId == "SUBS_ITEM" }?.let { productDetails ->
                manager.purchaseProduct(productDetails, selectedOfferIndex = 0)
            } ?: also {
                toast("구매 가능 한 상품이 없습니다.")
            }
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
