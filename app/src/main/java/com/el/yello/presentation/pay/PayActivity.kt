package com.el.yello.presentation.pay

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.el.yello.R
import com.el.yello.databinding.ActivityPayBinding
import com.el.yello.presentation.util.BillingCallback
import com.el.yello.presentation.util.BillingManager
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.data.model.request.pay.toRequestPayModel
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

@AndroidEntryPoint
class PayActivity : BindingActivity<ActivityPayBinding>(R.layout.activity_pay) {

    private val viewModel by viewModels<PayViewModel>()

    private var _adapter: PayAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private var _manager: BillingManager? = null
    private val manager
        get() = requireNotNull(_manager) { getString(R.string.manager_not_initialized_error_msg) }

    private var productDetailsList = listOf<ProductDetails>()

    private var paySubsDialog: PaySubsDialog? = null
    private var payInAppDialog: PayInAppDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initEvent()
        setBannerOnChangeListener()
        setBannerAutoScroll()
        setBillingManager()
        observeIsPurchasedStarted()
        observeCheckSubsState()
        observeCheckInAppState()
        observeCheckIsSubscribed()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPurchaseInfoFromServer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
        _manager?.billingClient?.endConnection()
        _manager = null
        payInAppDialog?.dismiss()
        paySubsDialog?.dismiss()
    }

    private fun initView() {
        _adapter = PayAdapter()
        binding.vpBanner.adapter = adapter
        binding.dotIndicator.setViewPager(binding.vpBanner)
        binding.tvOriginalPrice.paintFlags =
            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        viewModel.isFirstCreated = true
    }

    // BillingManager 설정 시 BillingClient 연결 & 콜백 응답 설정 -> 검증 진행
    private fun setBillingManager() {
        _manager = BillingManager(this, object : BillingCallback {
            override fun onBillingConnected() {
                lifecycleScope.launch {
                    manager.getProductDetails() { list -> productDetailsList = list }
                }
            }

            override fun onSuccess(purchase: Purchase) {
                if (purchase.products[0] == "yello_plus_subscribe") {
                    viewModel.checkSubsToServer(purchase.toRequestPayModel())
                } else {
                    viewModel.checkInAppToServer(purchase.toRequestPayModel())
                }
            }

            override fun onFailure(responseCode: Int) {
                Timber.d(responseCode.toString())
            }
        })
    }

    // 배너 자동 스크롤 로직
    private fun setBannerAutoScroll() {
        lifecycleScope.launch {
            while (true) {
                delay(2500)
                binding.vpBanner.currentItem.let {
                    binding.vpBanner.currentItem = it.plus(1) % 3
                }
            }
        }
    }

    //
    private fun setBannerOnChangeListener() {
        binding.vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var currentPosition = 0
            private var currentState = 0
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                handleScrollState(state)
                currentState = state
            }

            private fun handleScrollState(state: Int) {
                // 평소엔 1(DRAG), 2(SETTING), 0(IDLE) but 막혀있을 땐 1(DRAG), 0(IDLE), 2(SETTING) 이걸 이용
                if (state == ViewPager2.SCROLL_STATE_IDLE && currentState == ViewPager2.SCROLL_STATE_DRAGGING) {
                    setNextPage()
                }
            }

            private fun setNextPage() {
                val lastPosition = 2
                // 첫번째 화면이면 마지막 화면으로 이동
                if (currentPosition == 0) {
                    binding.vpBanner.currentItem = lastPosition
                    // 마지막 화면이면 첫번째 화면으로 이동
                } else if (currentPosition == lastPosition) {
                    binding.vpBanner.currentItem = 0
                }
            }
        })
    }

    private fun initEvent() {
        binding.clSubscribe.setOnSingleClickListener {
            viewModel.payCheck(0)
            AmplitudeUtils.trackEventWithProperties(
                "click_shop_buy", JSONObject().put("buy_type", "subscribe")
            )
            productDetailsList.withIndex().find { it.value.productId == YELLO_PLUS }
                ?.let { productDetails ->
                    manager.purchaseProduct(productDetails.index, productDetails.value)
                } ?: also {
                toast(getString(R.string.pay_error_no_item))
            }
        }

        binding.clNameCheckOne.setOnSingleClickListener {
            viewModel.payCheck(1)
            AmplitudeUtils.trackEventWithProperties(
                "click_shop_buy", JSONObject().put("buy_type", "ticket1")
            )
            productDetailsList.withIndex().find { it.value.productId == YELLO_ONE }
                ?.let { productDetails ->
                    manager.purchaseProduct(productDetails.index, productDetails.value)
                } ?: also {
                toast(getString(R.string.pay_error_no_item))
            }
        }

        binding.clNameCheckTwo.setOnSingleClickListener {
            viewModel.payCheck(2)
            AmplitudeUtils.trackEventWithProperties(
                "click_shop_buy", JSONObject().put("buy_type", "ticket2")
            )
            productDetailsList.withIndex().find { it.value.productId == YELLO_TWO }
                ?.let { productDetails ->
                    manager.purchaseProduct(productDetails.index, productDetails.value)
                } ?: also {
                toast(getString(R.string.pay_error_no_item))
            }
        }

        binding.clNameCheckFive.setOnSingleClickListener {
            viewModel.payCheck(3)
            AmplitudeUtils.trackEventWithProperties(
                "click_shop_buy", JSONObject().put("buy_type", "ticket5")
            )
            productDetailsList.withIndex().find { it.value.productId == YELLO_FIVE }
                ?.let { productDetails ->
                    manager.purchaseProduct(productDetails.index, productDetails.value)
                } ?: also {
                toast(getString(R.string.pay_error_no_item))
            }
        }

        binding.ivBack.setOnSingleClickListener {
            finish()
        }
    }

    // 구매 완료 이후 검증 완료까지 로딩 로티 실행
    private fun observeIsPurchasedStarted() {
        manager.isPurchaseStarted.observe(this) { boolean ->
            if (boolean == true) startLoadingScreen()
        }
    }

    // 구독 상품 검증 옵저버
    private fun observeCheckSubsState() {
        viewModel.postSubsCheckState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    AmplitudeUtils.trackEventWithProperties(
                        "complete_shop_buy",
                        JSONObject().put("buy_type", "subscribe").put("buy_price", "3900")
                    )
                    AmplitudeUtils.setUserDataProperties("user_buy_date")
                    stopLoadingScreen()
                    paySubsDialog = PaySubsDialog()
                    paySubsDialog?.show(supportFragmentManager, DIALOG_SUBS)
                }

                is UiState.Failure -> {
                    stopLoadingScreen()
                    if (state.msg == SERVER_ERROR) {
                        showErrorDialog()
                    } else {
                        toast(getString(R.string.pay_check_error))
                    }
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    // 인앱(소비성) 상품 검증 옵저버
    private fun observeCheckInAppState() {
        viewModel.postInAppCheckState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    stopLoadingScreen()
                    when (state.data?.productId) {
                        "yello_ticket_one" -> {
                            AmplitudeUtils.trackEventWithProperties(
                                "complete_shop_buy",
                                JSONObject().put("buy_type", "ticket1").put("buy_price", "1400")
                            )
                        }

                        "yello_ticket_two" -> {
                            AmplitudeUtils.trackEventWithProperties(
                                "complete_shop_buy",
                                JSONObject().put("buy_type", "ticket2").put("buy_price", "2800")
                            )
                        }

                        "yello_ticket_five" -> {
                            AmplitudeUtils.trackEventWithProperties(
                                "complete_shop_buy",
                                JSONObject().put("buy_type", "ticket5").put("buy_price", "5900")
                            )
                        }

                        else -> {
                            return@observe
                        }
                    }
                    AmplitudeUtils.setUserDataProperties("user_buy_date")
                    viewModel.currentInAppItem = state.data?.productId ?: ""
                    payInAppDialog = PayInAppDialog()
                    payInAppDialog?.show(supportFragmentManager, DIALOG_IN_APP)
                }

                is UiState.Failure -> {
                    stopLoadingScreen()
                    if (state.msg == SERVER_ERROR) {
                        showErrorDialog()
                    } else {
                        toast(getString(R.string.pay_check_error))
                    }
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    private fun startLoadingScreen() {
        binding.layoutPayCheckLoading.visibility = View.VISIBLE
        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        )
    }

    private fun stopLoadingScreen() {
        manager.setIsPurchasingOff()
        binding.layoutPayCheckLoading.visibility = View.GONE
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.pay_error_dialog_title))
            .setMessage(getString(R.string.pay_error_dialog_msg))
            .setPositiveButton(getString(R.string.pay_error_dialog_btn)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    // 구독 여부 확인해서 화면 표시 변경
    private fun observeCheckIsSubscribed() {
        viewModel.getPurchaseInfoState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data?.isSubscribe == true) {
                        binding.layoutShowSubs.visibility = View.VISIBLE
                    } else {
                        binding.layoutShowSubs.visibility = View.GONE
                    }
                }

                is UiState.Failure -> {
                    binding.layoutShowSubs.visibility = View.GONE
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    companion object {
        const val YELLO_PLUS = "yello_plus_subscribe"
        const val YELLO_ONE = "yello_ticket_one"
        const val YELLO_TWO = "yello_ticket_two"
        const val YELLO_FIVE = "yello_ticket_five"

        const val DIALOG_SUBS = "subsDialog"
        const val DIALOG_IN_APP = "inAppDialog"

        const val SERVER_ERROR = "HTTP 500 "
    }
}