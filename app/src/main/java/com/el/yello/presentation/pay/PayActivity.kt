package com.el.yello.presentation.pay

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.el.yello.BuildConfig.ADMOB_REWARD_KEY
import com.el.yello.R
import com.el.yello.databinding.ActivityPayBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.setting.SettingActivity
import com.el.yello.presentation.pay.dialog.PayInAppDialog
import com.el.yello.presentation.pay.dialog.PayPointDialog
import com.el.yello.presentation.pay.dialog.PaySubsDialog
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.data.model.request.pay.toRequestPayModel
import com.example.ui.activity.navigateTo
import com.example.ui.base.BindingActivity
import com.example.ui.context.colorOf
import com.example.ui.context.drawableOf
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
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
    private var payPointDialog: PayPointDialog? = null

    private var rewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initPurchaseBtnListener()
        initVoteBtnListener()
        viewModel.getRewardAdPossible()
        initAdBtnListener()
        observePostRewardAdState()
        observeRewardAdPossibleState()
        initBannerOnChangeListener()
        setBannerAutoScroll()
        viewModel.getUserDataFromServer()
        setBillingManager()
        observeIsPurchasing()
        observePurchaseInfoState()
        observeCheckSubsValidState()
        observeCheckInAppValidState()
        initPrivacyBtnListener()
        initServiceBtnListener()
    }

    private fun initView() {
        _adapter = PayAdapter()
        binding.vpBanner.adapter = adapter
        binding.dotIndicator.setViewPager(binding.vpBanner)
        binding.tvOriginalPrice.paintFlags =
            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.vm = viewModel
    }

    private fun initPurchaseBtnListener() {
        binding.layoutSubscribe.setOnSingleClickListener { startPurchase(TYPE_PLUS, YELLO_PLUS) }
        binding.layoutNameCheckOne.setOnSingleClickListener { startPurchase(TYPE_ONE, YELLO_ONE) }
        binding.layoutNameCheckTwo.setOnSingleClickListener { startPurchase(TYPE_TWO, YELLO_TWO) }
        binding.layoutNameCheckFive.setOnSingleClickListener { startPurchase(TYPE_FIVE, YELLO_FIVE) }
        binding.btnBack.setOnSingleClickListener { finish() }
    }

    private fun startPurchase(amplitude: String, productId: String) {
        setClickShopBuyAmplitude(amplitude)
        productDetailsList.withIndex().find { it.value.productId == productId }
            ?.let { productDetails ->
                manager.purchaseProduct(productDetails.index, productDetails.value)
            } ?: also {
            yelloSnackbar(binding.root, getString(R.string.pay_error_no_item))
        }
    }

    private fun initVoteBtnListener() {
        binding.layoutVoteForPoint.setOnSingleClickListener {
            navigateTo<MainActivity>()
            finish()
        }
    }

    private fun initAdBtnListener() {
        binding.layoutAdForPoint.setOnSingleClickListener {
            if (viewModel.isAdAvailable) {
                startLoadingScreen(AD)
                loadRewardAd()
            } else {
                yelloSnackbar(binding.root, getString(R.string.pay_ad_not_available_yet))
            }
        }
    }

    private fun loadRewardAd() {
        viewModel.setUuid()
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, ADMOB_REWARD_KEY, adRequest, object : RewardedAdLoadCallback() {

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                rewardedAd?.apply {
                    setSSV()
                    setRewardAdFinishCallback()
                }
                stopLoadingScreen(AD)

                rewardedAd?.show(this@PayActivity) {}
                    ?: toast(getString(R.string.pay_ad_fail_to_load))
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
                toast(getString(R.string.pay_ad_fail_to_load))
                stopLoadingScreen(AD)
            }
        })
    }

    private fun RewardedAd.setSSV() {
        setServerSideVerificationOptions(
            ServerSideVerificationOptions.Builder()
                .setCustomData(viewModel.idempotencyKey.toString())
                .build()
        )
    }

    private fun RewardedAd.setRewardAdFinishCallback() {
        fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                viewModel.postRewardAdToCheck()
                startLoadingScreen(AD)
            }
        }
    }

    private fun observePostRewardAdState() {
        viewModel.postRewardAdState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    stopLoadingScreen(AD)
                    payPointDialog = PayPointDialog()
                    payPointDialog?.show(supportFragmentManager, DIALOG_POINT)
                    viewModel.addPointCount(state.data?.rewardValue ?: 0)
                    binding.tvPointAmount.text = viewModel.pointCount.toString()
                    viewModel.getRewardAdPossible()
                }

                is UiState.Failure -> {
                    stopLoadingScreen(AD)
                    toast(getString(R.string.internet_connection_error_msg))
                }

                is UiState.Loading -> startLoadingScreen(AD)

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeRewardAdPossibleState() {
        viewModel.getRewardAdPossibleState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data) {
                        with(binding) {
                            layoutAdForPoint.background = drawableOf(R.drawable.shape_black_fill_purple_line_10_rect)
                            tvAdTitle.setTextColor(colorOf(R.color.purple_sub_100))
                            tvAdReward.setTextColor(colorOf(R.color.white))
                            tvAdTimeAvailable.visibility = View.VISIBLE
                            tvAdTimeRemain.visibility = View.INVISIBLE
                        }
                    } else {
                        with(binding) {
                            layoutAdForPoint.background = drawableOf(R.drawable.shape_black_fill_grayscales800_line_10_rect)
                            tvAdTitle.setTextColor(colorOf(R.color.grayscales_600))
                            tvAdReward.setTextColor(colorOf(R.color.grayscales_600))
                            tvAdTimeAvailable.visibility = View.INVISIBLE
                            tvAdTimeRemain.visibility = View.VISIBLE
                        }
                    }
                }

                is UiState.Failure -> {
                    toast(getString(R.string.internet_connection_error_msg))
                }

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
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
                        viewModel.checkSubsValidToServer(purchase.toRequestPayModel())
                    } else {
                        viewModel.checkInAppValidToServer(purchase.toRequestPayModel())
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

    private fun observeIsPurchasing() {
        manager.isPurchasing.flowWithLifecycle(lifecycle).onEach { isPurchasing ->
            if (isPurchasing) startLoadingScreen(PAY)
        }.launchIn(lifecycleScope)
    }

    private fun observePurchaseInfoState() {
        viewModel.getUserInfoState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data?.subscribe != SUBS_NORMAL) {
                        binding.layoutShowSubs.visibility = View.VISIBLE
                    } else {
                        binding.layoutShowSubs.visibility = View.INVISIBLE
                    }
                    viewModel.setTicketCount(state.data?.ticketCount ?: 0)
                    binding.tvKeyAmount.text = state.data?.ticketCount.toString()
                    viewModel.setPointCount(state.data?.point ?: 0)
                    binding.tvPointAmount.text = state.data?.point.toString()
                }

                is UiState.Failure -> {
                    binding.layoutShowSubs.isVisible = false
                }

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeCheckSubsValidState() {
        viewModel.postSubsCheckState.flowWithLifecycle(lifecycle).onEach { state ->
            stopLoadingScreen(PAY)
            when (state) {
                is UiState.Success -> {
                    setCompleteShopBuyAmplitude(TYPE_PLUS, PRICE_PLUS)
                    updateBuyDateAmplitude()
                    paySubsDialog = PaySubsDialog()
                    paySubsDialog?.show(supportFragmentManager, DIALOG_SUBS)
                }

                is UiState.Failure -> {
                    stopLoadingScreen(PAY)
                    if (state.msg == SERVER_ERROR) {
                        showErrorDialog()
                    } else {
                        yelloSnackbar(binding.root, getString(R.string.pay_check_error))
                    }
                }

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeCheckInAppValidState() {
        viewModel.postInAppCheckState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    stopLoadingScreen(PAY)
                    when (state.data?.productId) {
                        YELLO_ONE -> {
                            setCompleteShopBuyAmplitude(TYPE_ONE, PRICE_ONE)
                            viewModel.addTicketCount(1)
                        }

                        YELLO_TWO -> {
                            setCompleteShopBuyAmplitude(TYPE_TWO, PRICE_TWO)
                            viewModel.addTicketCount(2)
                        }

                        YELLO_FIVE -> {
                            setCompleteShopBuyAmplitude(TYPE_FIVE, PRICE_FIVE)
                            viewModel.addTicketCount(5)
                        }

                        else -> return@onEach
                    }
                    updateBuyDateAmplitude()
                    viewModel.currentInAppItem = state.data?.productId.toString()
                    payInAppDialog = PayInAppDialog()
                    payInAppDialog?.show(supportFragmentManager, DIALOG_IN_APP)
                }

                is UiState.Failure -> {
                    stopLoadingScreen(PAY)
                    if (state.msg == SERVER_ERROR) {
                        showErrorDialog()
                    } else {
                        yelloSnackbar(binding.root, getString(R.string.pay_check_error))
                    }
                }

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun initServiceBtnListener() {
        binding.btnPayGuideService.setOnSingleClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(SettingActivity.SERVICE_URL)),
            )
        }
    }

    private fun initPrivacyBtnListener() {
        binding.btnPayGuidePrivacy.setOnSingleClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(SettingActivity.PRIVACY_URL)),
            )
        }
    }

    private fun startLoadingScreen(type: String) {
        if (type == PAY) {
            binding.layoutPayCheckLoading.isVisible = true
        } else {
            binding.layoutAdCheckLoading.isVisible = true
        }
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        )
    }

    private fun stopLoadingScreen(type: String) {
        if (type == PAY) {
            manager.setIsPurchasing(false)
            binding.layoutPayCheckLoading.isVisible = false
        } else {
            binding.layoutAdCheckLoading.isVisible = false
        }
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

    private fun updateBuyDateAmplitude() {
        AmplitudeUtils.setUserDataProperties("user_buy_date")
    }

    override fun finish() {
        super.finish()
        intent.putExtra("ticketCount", viewModel.ticketCount)
        setResult(RESULT_OK, intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
        _manager?.billingClient?.endConnection()
        _manager = null
        payInAppDialog?.dismiss()
        paySubsDialog?.dismiss()
        payPointDialog?.dismiss()
        rewardedAd = null
    }

    companion object {
        const val YELLO_PLUS = "yello_plus_subscribe"
        const val YELLO_ONE = "yello_ticket_one"
        const val YELLO_TWO = "yello_ticket_two"
        const val YELLO_FIVE = "yello_ticket_five"

        const val TYPE_PLUS = "subscribe"
        const val TYPE_ONE = "ticket1"
        const val TYPE_TWO = "ticket2"
        const val TYPE_FIVE = "ticket5"

        const val PRICE_PLUS = "2800"
        const val PRICE_ONE = "990"
        const val PRICE_TWO = "1900"
        const val PRICE_FIVE = "3900"

        const val DIALOG_SUBS = "subsDialog"
        const val DIALOG_IN_APP = "inAppDialog"
        const val DIALOG_POINT = "pointDialog"

        const val SERVER_ERROR = "HTTP 500 "

        const val SUBS_NORMAL = "normal"

        const val AD = "AD"
        const val PAY = "PAY"
    }
}
