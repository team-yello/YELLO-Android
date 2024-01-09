package com.el.yello.presentation.main.myyello

import android.app.Activity
import android.content.Intent
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
import com.el.yello.R
import com.el.yello.databinding.FragmentMyYelloBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity
import com.el.yello.presentation.pay.PayActivity
import com.el.yello.presentation.util.BaseLinearRcvItemDeco
import com.el.yello.util.Utils.setPullToScrollColor
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AmplitudeUtils.trackEventWithProperties("view_all_messages")
        initView()
        initEvent()
        observe()
        initPullToScrollListener()
    }

    private fun initView() {
        viewModel.getMyYelloList()
        adapter = MyYelloAdapter { it, pos ->
            viewModel.setPosition(pos)
            myYelloReadActivityLauncher.launch(
                MyYelloReadActivity.getIntent(
                    requireContext(),
                    it.id,
                    it.nameHint,
                    it.isHintUsed,
                ),
            )
        }
        binding.rvMyYelloReceive.addItemDecoration(
            BaseLinearRcvItemDeco(8, 8, 0, 0, 5, RecyclerView.VERTICAL, 110),
        )
        adapter?.setHasStableIds(true)
        binding.rvMyYelloReceive.adapter = adapter

        infinityScroll()
    }

    private fun initEvent() {
        binding.btnSendCheck.setOnSingleClickListener {
            setClickGoShopAmplitude("cta_main")
            Intent(requireContext(), PayActivity::class.java).apply {
                payActivityLauncher.launch(this)
            }
        }

        binding.clSendOpen.setOnSingleClickListener {
            yelloSnackbar(binding.root, "무슨 쪽지가 궁금한가요?")
        }

        binding.btnShop.setOnSingleClickListener {
            setClickGoShopAmplitude("message_shop")
            goToPayActivity()
        }
    }

    private fun observe() {
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
                    AmplitudeUtils.trackEventWithProperties("scroll_all_messages")
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
                val isHintUsed = intent.getBooleanExtra("isHintUsed", false)
                val nameIndex = intent.getIntExtra("nameIndex", -1)
                val ticketCount = intent.getIntExtra("ticketCount", -1)
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
                val ticketCount = intent.getIntExtra("ticketCount", -1)
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
                    delay(200)
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

    private fun setClickGoShopAmplitude(value: String) {
        AmplitudeUtils.trackEventWithProperties(
            "click_go_shop",
            JSONObject().put("shop_button", value),
        )
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}
