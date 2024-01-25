package com.el.yello.presentation.main.look

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentLookBinding
import com.el.yello.presentation.main.dialog.InviteFriendDialog
import com.el.yello.presentation.util.BaseLinearRcvItemDeco
import com.el.yello.util.Utils.setPullToScrollColor
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject

@AndroidEntryPoint
class LookFragment : BindingFragment<FragmentLookBinding>(R.layout.fragment_look) {

    private var _adapter: LookPageAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by viewModels<LookViewModel>()

    private var inviteFriendDialog: InviteFriendDialog? = null

    private var isScrolled: Boolean = false
    private var isNoFriend: Boolean = false

    private var isFilterSelected: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initInviteBtnListener()
        initFilterBtnListener()
        setListBottomPadding()
        observeTimelinePagingList(isFilterSelected)
        setPullToScrollListener()
        observePagingLoadingState()
        catchScrollForAmplitude()
        AmplitudeUtils.trackEventWithProperties("view_timeline")
    }

    private fun initAdapter() {
        viewModel.setFirstLoading(true)
        _adapter = LookPageAdapter()
        adapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.prepend.endOfPaginationReached) {
                binding.layoutLookNoFriendsList.isVisible = adapter.itemCount < 1
                binding.layoutLookListInfo.isGone = adapter.itemCount < 1
                binding.rvLook.isGone = adapter.itemCount < 1
                isNoFriend = adapter.itemCount < 1
            }
        }
        binding.rvLook.adapter = adapter
    }

    private fun initInviteBtnListener() {
        binding.btnLookNoFriend.setOnSingleClickListener {
            inviteFriendDialog =
                InviteFriendDialog.newInstance(viewModel.getYelloId(), TIMELINE_NO_FRIEND)
            AmplitudeUtils.trackEventWithProperties(
                "click_invite", JSONObject().put("invite_view", TIMELINE_NO_FRIEND)
            )
            inviteFriendDialog?.show(parentFragmentManager, INVITE_DIALOG)
        }
    }

    private fun initFilterBtnListener() {
        binding.btnLookFilter.setOnSingleClickListener {
            isFilterSelected = !isFilterSelected
            observeTimelinePagingList(isFilterSelected)
            binding.tvLookFilterType.text = if(isFilterSelected) TYPE_MINE else TYPE_ALL
        }
    }

    private fun setListBottomPadding() {
        binding.rvLook.addItemDecoration(BaseLinearRcvItemDeco(bottomPadding = 14))
    }

    private fun setPullToScrollListener() {
        binding.layoutLookSwipe.apply {
            setOnRefreshListener {
                adapter.refresh()
                viewModel.setFirstLoading(true)
            }
            setPullToScrollColor(R.color.grayscales_500, R.color.grayscales_700)
        }
        adapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChangedBy { it.refresh }.onEach {
                delay(200)
                binding.layoutLookSwipe.isRefreshing = false
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeTimelinePagingList(onlyMine: Boolean) {
        viewModel.getLookListWithPaging(onlyMine).flowWithLifecycle(lifecycle).onEach { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }.launchIn(lifecycleScope)
    }


    private fun observePagingLoadingState() {
        adapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        if (!isNoFriend) startShimmerView()
                    }

                    is LoadState.NotLoading -> {
                        if (viewModel.isFirstLoading.value) {
                            startFadeIn()
                            viewModel.setFirstLoading(false)
                        }
                        stopShimmerView()
                    }

                    is LoadState.Error -> {
                        startShimmerView()
                        yelloSnackbar(requireView(), getString(R.string.look_error_friend_list))
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun catchScrollForAmplitude() {
        binding.rvLook.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isScrolled) {
                    AmplitudeUtils.trackEventWithProperties("scroll_profile_friends")
                    isScrolled = true
                }
            }
        })
    }

    private fun startFadeIn() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.rvLook.startAnimation(animation)
    }

    private fun startShimmerView() {
        with(binding) {
            shimmerLookList.startShimmer()
            shimmerLookList.isVisible = true
            rvLook.isVisible = false
        }
    }

    private fun stopShimmerView() {
        with(binding) {
            shimmerLookList.stopShimmer()
            shimmerLookList.isVisible = false
            rvLook.isVisible = true
        }
    }

    fun scrollToTop() {
        binding.rvLook.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }

    companion object {
        const val INVITE_DIALOG = "inviteDialog"
        const val TIMELINE_NO_FRIEND = "timeline_0friend"

        const val TYPE_ALL = "모든 쪽지"
        const val TYPE_MINE = "내가 보낸 쪽지"
    }
}