package com.yello.presentation.yello

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.yello.R
import com.yello.databinding.ActivityYelloBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class YelloActivity : BindingActivity<ActivityYelloBinding>(R.layout.activity_yello) {
    private val viewModel by viewModels<YelloViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        observe()
    }

    private fun initView() {
        viewModel.getMyYelloList()
        viewModel.getYelloDetail()
        viewModel.checkKeyword()
        viewModel.checkName()
    }

    private fun observe() {
        viewModel.myYelloData.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        toast(it.data.toString())
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)

        viewModel.yelloDetailData.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        toast(it.data.toString())
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)

        viewModel.keywordData.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        toast(it.data.toString())
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)

        viewModel.nameData.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        toast(it.data.toString())
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }
}