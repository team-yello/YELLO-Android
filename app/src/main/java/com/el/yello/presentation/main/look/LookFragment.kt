package com.el.yello.presentation.main.look

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentLookBinding
import com.el.yello.presentation.main.recommend.RecommendInviteDialog
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class LookFragment : BindingFragment<FragmentLookBinding>(R.layout.fragment_look) {

    private var _adapter: LookAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by viewModels<LookViewModel>()

    private var inviteFriendDialog: RecommendInviteDialog? = null

    private var isScrolled: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapterWithFirstList()
        initNoFriendScreenInviteBtnListener()
        initPullToScrollListener()
        getTimelinePagingList()
        observeIsLoading()
        observeErrorResult()
        catchScrollForAmplitude()
        AmplitudeUtils.trackEventWithProperties("view_timeline")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }

    private fun initAdapterWithFirstList() {
        viewModel.setNotLoading()
        _adapter = LookAdapter()
        binding.rvLook.adapter = adapter

        adapter.addLoadStateListener { combinedLoadStates ->
            if(combinedLoadStates.prepend.endOfPaginationReached && viewModel.isLoading.value == false) {
                binding.layoutLookNoFriendsList.isVisible = adapter.itemCount < 1
                binding.rvLook.isGone = adapter.itemCount < 1
            }
        }
    }

    private fun initNoFriendScreenInviteBtnListener() {
        binding.btnLookNoFriend.setOnSingleClickListener {
            inviteFriendDialog = RecommendInviteDialog.newInstance(viewModel.getYelloId(), TIMELINE_NO_FRIEND)
            AmplitudeUtils.trackEventWithProperties(
                "click_invite",
                JSONObject().put("invite_view", TIMELINE_NO_FRIEND)
            )
            inviteFriendDialog?.show(parentFragmentManager, INVITE_DIALOG)
        }
    }

    private fun initPullToScrollListener() {
        binding.layoutLookSwipe.apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    _adapter = null
                    initAdapterWithFirstList()
                    getTimelinePagingList()
                    binding.layoutLookSwipe.isRefreshing = false
                }
            }
            setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, R.color.grayscales_700))
            setColorSchemeColors(ContextCompat.getColor(context, R.color.grayscales_500))
        }
    }

    private fun getTimelinePagingList() {
        lifecycleScope.launch {
            viewModel.getLookListWithPaging()
                .flowWithLifecycle(lifecycle)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
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

    private fun observeErrorResult() {
        viewModel.getErrorResult.observe(viewLifecycleOwner) {
            yelloSnackbar(requireView(), getString(R.string.look_error_friend_list))
            startShimmerView()
        }
    }

    private fun observeIsLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            if (state) {
                startShimmerView()
            } else {
                stopShimmerView()
            }
        }
    }

    private fun startShimmerView() {
        binding.shimmerLookList.startShimmer()
        binding.shimmerLookList.visibility = View.VISIBLE
        binding.layoutLookSwipe.visibility = View.GONE
    }

    private fun stopShimmerView() {
        binding.shimmerLookList.stopShimmer()
        binding.shimmerLookList.visibility = View.GONE
        binding.layoutLookSwipe.visibility = View.VISIBLE
    }

    fun scrollToTop() {
        binding.rvLook.smoothScrollToPosition(0)
    }

    companion object {

        const val INVITE_DIALOG = "inviteDialog"
        const val TIMELINE_NO_FRIEND = "timeline_0friend"

        @JvmStatic
        fun newInstance() = LookFragment()
    }
}