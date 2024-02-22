package com.el.yello.presentation.main.myyello

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.BuildConfig.ADMOB_FULLSCREEN_KEY
import com.el.yello.R
import com.el.yello.databinding.FragmentMyYelloBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity.Companion.EVENT_CLICK_GO_SHOP
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity.Companion.EXTRA_IS_HINT_USED
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity.Companion.EXTRA_NAME_INDEX
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity.Companion.EXTRA_TICKET_COUNT
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity.Companion.JSON_SHOP_BUTTON
import com.el.yello.presentation.pay.PayActivity
import com.el.yello.util.extension.BaseLinearRcvItemDeco
import com.el.yello.util.extension.setPullToScrollColor
import com.el.yello.util.manager.AmplitudeManager
import com.el.yello.util.extension.yelloSnackbar
import com.example.domain.entity.Yello
import com.example.ui.base.BindingFragment
import com.example.ui.state.UiState
import com.example.ui.extension.setOnSingleClickListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class MyYelloFragment : BindingFragment<FragmentMyYelloBinding>(R.layout.fragment_my_yello) {

    private val viewModel by viewModels<MyYelloViewModel>()
    private var adapter: MyYelloAdapter? = null
    private var isScrolled: Boolean = false

    private var interstitialAd: InterstitialAd? = null
    private var clickedYello: Yello? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AmplitudeManager.trackEventWithProperties(EVENT_VIEW_ALL_MESSAGES)
        initView()
        initEvent()
        loadRewardAd()
        observe()
        initPullToScrollListener()
    }

    private fun initView() {
        adapter = MyYelloAdapter { it, pos ->
            viewModel.setPosition(pos)
            clickedYello = it
            loadThirdReadWithAd()
        }
        binding.rvMyYelloReceive.addItemDecoration(
            BaseLinearRcvItemDeco(8, 8, 0, 0, 5, RecyclerView.VERTICAL, 110),
        )
        adapter?.setHasStableIds(true)
        binding.rvMyYelloReceive.adapter = adapter

        infinityScroll()
    }

    private fun navigateToMyYelloReadActivity() {
        if (clickedYello != null) {
            myYelloReadActivityLauncher.launch(
                MyYelloReadActivity.getIntent(
                    requireContext(),
                    clickedYello?.id ?: 0,
                    clickedYello?.nameHint,
                    clickedYello?.isHintUsed,
                ),
            )
        }
    }

    private fun initEvent() {
        binding.btnSendCheck.setOnSingleClickListener {
            setClickGoShopAmplitude(VALUE_CTA_MAIN)
            Intent(requireContext(), PayActivity::class.java).apply {
                payActivityLauncher.launch(this)
            }
        }

        binding.clSendOpen.setOnSingleClickListener {
            yelloSnackbar(binding.root, getString(R.string.my_yello_send_open_click))
        }

        binding.btnShop.setOnSingleClickListener {
            setClickGoShopAmplitude(VALUE_MESSAGE_SHOP)
            goToPayActivity()
        }
    }

    private fun setClickGoShopAmplitude(value: String) {
        AmplitudeManager.trackEventWithProperties(
            EVENT_CLICK_GO_SHOP,
            JSONObject().put(JSON_SHOP_BUTTON, value),
        )
    }

    private fun loadRewardAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),
            ADMOB_FULLSCREEN_KEY, adRequest, object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    interstitialAd?.setRewardAdFinishCallback()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }
            })
    }

    private fun InterstitialAd.setRewardAdFinishCallback() {
        fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                navigateToMyYelloReadActivity()
                interstitialAd = null
                loadRewardAd()
            }
        }
    }

    private fun loadThirdReadWithAd() {
        viewModel.addReadCount()
        if (viewModel.readCount % 5 == 3 && interstitialAd != null) {
            interstitialAd?.show(requireActivity())
        } else {
            navigateToMyYelloReadActivity()
        }
    }

    private fun observe() {
        setupMyYelloData()
        setupTotalCount()
        setupGetBannerState()
    }

    private fun setupMyYelloData() {
        viewModel.myYelloData.observe(viewLifecycleOwner) { state ->
            binding.uiState = state.getUiStateModel()
            when (state) {
                is UiState.Success -> {
                    binding.shimmerMyYelloReceive.stopShimmer()
                    if (viewModel.isFirstLoading) {
                        startFadeIn()
                        viewModel.isFirstLoading = false
                    }
                    binding.clSendOpen.isVisible = state.data.ticketCount != 0
                    binding.btnSendCheck.isVisible = state.data.ticketCount == 0
                    binding.tvKeyNumber.text = state.data.ticketCount.toString()
                    adapter?.addItem(state.data.yello)
                }

                is UiState.Failure -> {
                    binding.shimmerMyYelloReceive.stopShimmer()
                    yelloSnackbar(requireView(), state.msg)
                }

                is UiState.Empty -> binding.shimmerMyYelloReceive.stopShimmer()

                is UiState.Loading -> binding.shimmerMyYelloReceive.startShimmer()
            }
        }
    }

    private fun setupGetBannerState() {
        viewModel.getBannerState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        with(state.data) {
                            if (!isAvailable) return@onEach

                            binding.layoutMyYelloBanner.visibility = View.VISIBLE
                            binding.tvMyYelloBanner.text = title
                            if (redirectUrl.isBlank()) {
                                binding.tvMyYelloClickMe.visibility = View.GONE
                                return@onEach
                            }
                            binding.layoutMyYelloBanner.setOnSingleClickListener {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl)))
                            }
                            binding.tvMyYelloBanner.setOnSingleClickListener {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl)))
                            }
                            binding.tvMyYelloClickMe.setOnSingleClickListener {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl)))
                            }
                        }
                    }

                    is UiState.Failure -> {
                        yelloSnackbar(
                            binding.root,
                            getString(R.string.internet_connection_error_msg)
                        )
                    }

                    is UiState.Empty -> {
                        yelloSnackbar(
                            binding.root,
                            getString(R.string.internet_connection_error_msg)
                        )
                    }

                    is UiState.Loading -> return@onEach
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupTotalCount() {
        viewModel.totalCount.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            binding.tvCount.text = it.toString()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.voteCount.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            when (it) {
                is UiState.Success -> (activity as? MainActivity)?.setBadgeCount(it.data.totalCount)

                is UiState.Failure -> yelloSnackbar(binding.root, it.msg)

                else -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun infinityScroll() {
        binding.rvMyYelloReceive.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !binding.rvMyYelloReceive.canScrollVertically(1) && (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == adapter!!.itemCount - 1) {
                    viewModel.getMyYelloList()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isScrolled) {
                    AmplitudeManager.trackEventWithProperties(EVENT_SCROLL_ALL_MESSAGES)
                    isScrolled = true
                }
            }
        })
    }

    private fun goToPayActivity() {
        Intent(requireContext(), PayActivity::class.java).apply {
            payActivityLauncher.launch(this)
        }
    }

    private val myYelloReadActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { intent ->
                val isHintUsed = intent.getBooleanExtra(EXTRA_IS_HINT_USED, false)
                val nameIndex = intent.getIntExtra(EXTRA_NAME_INDEX, -1)
                val ticketCount = intent.getIntExtra(EXTRA_TICKET_COUNT, -1)
                val list = adapter?.currentList()
                val selectItem = list?.get(viewModel.position)
                selectItem?.apply {
                    this.isRead = true
                    this.isHintUsed = isHintUsed
                    this.nameHint = nameIndex
                }
                selectItem?.let {
                    adapter?.changeItem(viewModel.position, selectItem)
                }
                if (ticketCount != -1) {
                    binding.tvKeyNumber.text = ticketCount.toString()
                }

                binding.clSendOpen.isVisible = ticketCount != 0
                binding.btnSendCheck.isVisible = ticketCount == 0

                viewModel.getVoteCount()
            }
        }
    }

    private val payActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { intent ->
                val ticketCount = intent.getIntExtra(EXTRA_TICKET_COUNT, -1)
                if (ticketCount != -1) {
                    binding.tvKeyNumber.text = ticketCount.toString()
                }
                binding.clSendOpen.isVisible = ticketCount != 0
                binding.btnSendCheck.isVisible = ticketCount == 0
            }
        }
    }

    private fun initPullToScrollListener() {
        binding.layoutMyYelloSwipe.apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    adapter?.clearList()
                    viewModel.setToFirstPage()
                    viewModel.getMyYelloList()
                    delay(DELAY_MY_YELLO_SWIPE)
                    binding.layoutMyYelloSwipe.isRefreshing = false
                }
            }
            setPullToScrollColor(R.color.grayscales_500, R.color.grayscales_700)
        }
    }

    private fun startFadeIn() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.rvMyYelloReceive.startAnimation(animation)
    }

    fun scrollToTop() {
        binding.rvMyYelloReceive.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        adapter = null
        interstitialAd = null
        super.onDestroyView()
    }

    companion object {
        private const val EVENT_VIEW_ALL_MESSAGES = "view_all_messages"
        private const val VALUE_CTA_MAIN = "cta_main"
        private const val VALUE_MESSAGE_SHOP = "message_shop"
        private const val EVENT_SCROLL_ALL_MESSAGES = "scroll_all_messages"
        private const val DELAY_MY_YELLO_SWIPE = 200L
    }
}
