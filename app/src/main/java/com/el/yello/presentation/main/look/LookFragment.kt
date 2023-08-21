package com.el.yello.presentation.main.look

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentLookBinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LookFragment : BindingFragment<FragmentLookBinding>(R.layout.fragment_look) {

    private var _adapter: LookAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by viewModels<LookViewModel>()

    private var isScrolled: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapterWithFirstList()
        getSearchPagingList()
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
        viewModel.setFirstPageLoading()
        _adapter = LookAdapter()
        binding.rvLook.adapter = adapter

        adapter.addLoadStateListener { combinedLoadStates ->
            if(combinedLoadStates.prepend.endOfPaginationReached) {
                binding.layoutLookNoFriendsList.isVisible = adapter.itemCount < 1
                binding.rvLook.isGone = adapter.itemCount < 1
            }
        }
    }

    private fun getSearchPagingList() {
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
        binding.rvLook.visibility = View.GONE
    }

    private fun stopShimmerView() {
        binding.shimmerLookList.stopShimmer()
        binding.shimmerLookList.visibility = View.GONE
        binding.rvLook.visibility = View.VISIBLE
    }

    fun scrollToTop() {
        binding.rvLook.smoothScrollToPosition(0)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LookFragment()
    }
}