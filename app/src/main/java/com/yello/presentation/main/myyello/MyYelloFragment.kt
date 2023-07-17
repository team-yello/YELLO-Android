package com.yello.presentation.main.myyello

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.yello.R
import com.yello.databinding.FragmentMyYelloBinding
import com.yello.presentation.main.myyello.read.MyYelloReadActivity
import com.yello.presentation.util.BaseLinearRcvItemDeco
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MyYelloFragment : BindingFragment<FragmentMyYelloBinding>(R.layout.fragment_my_yello) {
    private val viewModel by viewModels<MyYelloViewModel>()
    private var adapter: MyYelloAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
    }

    private fun initView() {
        viewModel.getMyYelloList()
        adapter = MyYelloAdapter {
            startActivity(MyYelloReadActivity.getIntent(requireContext(), it.id))
        }
        binding.rvMyYelloReceive.addItemDecoration(
            BaseLinearRcvItemDeco(
                8,
                8,
                0,
                0,
                5,
                RecyclerView.VERTICAL,
                94
            )
        )
        binding.rvMyYelloReceive.adapter = adapter

        infinityScroll()
    }

    private fun observe() {
        viewModel.myYelloData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.uiState = it.getUiStateModel()
                when (it) {
                    is UiState.Success -> {
                        adapter?.addItem(it.data.yello)
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {}
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.totalCount.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.tvCount.text = it.toString()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    // 페이지네이션
    private fun infinityScroll() {
        binding.rvMyYelloReceive.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (!binding.rvMyYelloReceive.canScrollVertically(1) &&
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == adapter!!.itemCount - 1
                    ) {
                        viewModel.getMyYelloList()
                    }
            }
        }
    })
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}
