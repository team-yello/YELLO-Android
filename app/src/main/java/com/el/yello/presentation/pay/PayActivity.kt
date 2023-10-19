package com.el.yello.presentation.pay

import android.graphics.Paint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.el.yello.R
import com.el.yello.databinding.ActivityPayBinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.data.model.request.pay.toRequestPayModel
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        initPurchaseBtnListener()
        initBannerOnChangeListener()
        viewModel.getPurchaseInfoFromServer()
        setBannerAutoScroll()
        setBillingManager()
        observeIsPurchasing()
        observePurchaseInfoState()
        observeCheckSubsState()
        observeCheckInAppState()
    }

    private fun initView() {
        _adapter = PayAdapter()
        binding.vpBanner.adapter = adapter
        binding.dotIndicator.setViewPager(binding.vpBanner)
        binding.tvOriginalPrice.paintFlags =
            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun initPurchaseBtnListener() {
        binding.clSubscribe.setOnSingleClickListener { startPurchase("subscribe", YELLO_PLUS) }
        binding.clNameCheckOne.setOnSingleClickListener { startPurchase("ticket1", YELLO_ONE) }
        binding.clNameCheckTwo.setOnSingleClickListener { startPurchase("ticket2", YELLO_TWO) }
        binding.clNameCheckFive.setOnSingleClickListener { startPurchase("ticket5", YELLO_FIVE) }
        binding.ivBack.setOnSingleClickListener { finish() }
    }

    private fun startPurchase(amplitude: String, productId: String) {
        setClickShopBuyAmplitude(amplitude)
        productDetailsList.withIndex().find { it.value.productId == productId }
            ?.let { productDetails ->
                manager.purchaseProduct(productDetails.index, productDetails.value)
            } ?: also {
            toast(getString(R.string.pay_error_no_item))
        }
    }

    // BillingManager 설정 시 BillingClient 연결 & 콜백 응답 설정 -> 검증 진행
    private fun setBillingManager() {
        _manager = BillingManager(
            this,
            object : BillingCallback {
                override fun onBillingConnected() {
                    lifecycleScope.launch {
                        manager.getProductDetails() { list -> productDetailsList = list }
                    }
                }

                override fun onSuccess(purchase: Purchase) {
                    if (purchase.products[0] == YELLO_PLUS) {
                        viewModel.checkSubsToServer(purchase.toRequestPayModel())
                    } else {
                        viewModel.checkInAppToServer(purchase.toRequestPayModel())
                    }
                }

                override fun onFailure(responseCode: Int) {
                    Timber.d(responseCode.toString())
                }
            },
        )
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

    private fun initBannerOnChangeListener() {
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
                if (currentPosition == 0) {
                    binding.vpBanner.currentItem = lastPosition
                } else if (currentPosition == lastPosition) {
                    binding.vpBanner.currentItem = 0
                }
            }
        })
    }

    // 구매 완료 이후 검증 완료까지 로딩 로티 실행
    private fun observeIsPurchasing() {
        manager.isPurchasing.flowWithLifecycle(lifecycle).onEach { isPurchasing ->
            if (isPurchasing) startLoadingScreen()
        }.launchIn(lifecycleScope)
    }

    // 구독 여부 확인해서 화면 표시 변경
    private fun observePurchaseInfoState() {
        viewModel.getPurchaseInfoState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.layoutShowSubs.isVisible = state.data?.isSubscribe == true
                    viewModel.setTicketCount(state.data?.ticketCount ?: 0)
                }

                is UiState.Failure -> {
                    binding.layoutShowSubs.isVisible = false
                }

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    // 구독 상품 검증 옵저버
    private fun observeCheckSubsState() {
        viewModel.postSubsCheckState.flowWithLifecycle(lifecycle).onEach { state ->
                stopLoadingScreen()
                when (state) {
                    is UiState.Success -> {
                        setCompleteShopBuyAmplitude("subscribe", "3900")
                        paySubsDialog = PaySubsDialog()
                        paySubsDialog?.show(supportFragmentManager, DIALOG_SUBS)
                        AmplitudeUtils.setUserDataProperties("user_buy_date")
                    }

                    is UiState.Failure -> {
                        stopLoadingScreen()
                        if (state.msg == SERVER_ERROR) {
                            showErrorDialog()
                        } else {
                            toast(getString(R.string.pay_check_error))
                        }
                    }

                    is UiState.Loading -> return@onEach

                    is UiState.Empty -> return@onEach
                }
            }.launchIn(lifecycleScope)
    }

    // 인앱(소비성) 상품 검증 옵저버
    private fun observeCheckInAppState() {
        viewModel.postInAppCheckState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    stopLoadingScreen()
                    when (state.data?.productId) {
                        YELLO_ONE -> {
                            setCompleteShopBuyAmplitude("ticket1", "1400")
                            viewModel.addTicketCount(1)
                        }

                        YELLO_TWO -> {
                            setCompleteShopBuyAmplitude("ticket2", "2800")
                            viewModel.addTicketCount(2)
                        }

                        YELLO_FIVE -> {
                            setCompleteShopBuyAmplitude("ticket5", "5900")
                            viewModel.addTicketCount(5)
                        }

                        else -> return@onEach
                    }
                    viewModel.currentInAppItem = state.data?.productId.toString()
                    payInAppDialog = PayInAppDialog()
                    payInAppDialog?.show(supportFragmentManager, DIALOG_IN_APP)
                    AmplitudeUtils.setUserDataProperties("user_buy_date")
                }

                is UiState.Failure -> {
                    stopLoadingScreen()
                    if (state.msg == SERVER_ERROR) {
                        showErrorDialog()
                    } else {
                        toast(getString(R.string.pay_check_error))
                    }
                }

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun startLoadingScreen() {
        binding.layoutPayCheckLoading.isVisible = true
        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        )
    }

    private fun stopLoadingScreen() {
        manager.setIsPurchasing(false)
        binding.layoutPayCheckLoading.isVisible = false
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    // 서버 오류(500) 시 시스템 다이얼로그 띄우기
    private fun showErrorDialog() {
        AlertDialog.Builder(this).setTitle(getString(R.string.pay_error_dialog_title))
            .setMessage(getString(R.string.pay_error_dialog_msg))
            .setPositiveButton(getString(R.string.pay_error_dialog_btn)) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    private fun setClickShopBuyAmplitude(buyType: String) {
        AmplitudeUtils.trackEventWithProperties(
            "click_shop_buy",
            JSONObject().put("buy_type", buyType),
        )
    }

    private fun setCompleteShopBuyAmplitude(buyType: String, buyPrice: String) {
        AmplitudeUtils.trackEventWithProperties(
            "complete_shop_buy",
            JSONObject().put("buy_type", buyType).put("buy_price", buyPrice),
        )
    }

    override fun finish() {
        intent.putExtra("ticketCount", viewModel.ticketCount)
        setResult(RESULT_OK, intent)
        super.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
        _manager?.billingClient?.endConnection()
        _manager = null
        payInAppDialog?.dismiss()
        paySubsDialog?.dismiss()
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
