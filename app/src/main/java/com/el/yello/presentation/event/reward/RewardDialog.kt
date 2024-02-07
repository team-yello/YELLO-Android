package com.el.yello.presentation.event.reward

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.el.yello.R
import com.el.yello.databinding.FragmentRewardDialogBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RewardDialog :
    BindingDialogFragment<FragmentRewardDialogBinding>(R.layout.fragment_reward_dialog) {
    private val viewModel by viewModels<RewardViewModel>()

    override fun onStart() {
        super.onStart()

        initContainerLayoutTransparent()
        setupPostEventState()
    }

    private fun initContainerLayoutTransparent() {
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initConfirmBtnClickListener()
    }

    private fun initConfirmBtnClickListener() {
        binding.tvRewardConfirm.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun setupPostEventState() {
        viewModel.postEventState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        binding.tvRewardTitle.text = state.data.title
                        binding.ivReward.load(state.data.imageUrl)
                    }

                    is UiState.Failure -> dismiss()
                    is UiState.Empty -> return@onEach
                    is UiState.Loading -> return@onEach
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RewardDialog()
    }
}
