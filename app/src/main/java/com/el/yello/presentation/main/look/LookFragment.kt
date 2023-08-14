package com.el.yello.presentation.main.look

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentLookBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapterWithFirstList()
        getSearchPagingList()
        observeIsLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }

    private fun initAdapterWithFirstList() {
        viewModel.setFirstPageLoading()
        _adapter = LookAdapter()
        binding.rvLook.adapter = adapter
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