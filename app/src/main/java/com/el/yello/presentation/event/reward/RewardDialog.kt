package com.el.yello.presentation.event.reward

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.el.yello.R
import com.el.yello.databinding.FragmentRewardDialogBinding
import com.el.yello.presentation.event.EventViewModel
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RewardDialog :
    BindingDialogFragment<FragmentRewardDialogBinding>(R.layout.fragment_reward_dialog) {
    private val activityViewModel by activityViewModels<EventViewModel>()
    private val viewModel by viewModels<RewardViewModel>()

    override fun onStart() {
        super.onStart()

        setupPostEventState()
        postEvent()
        initContainerLayoutTransparent()
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

    private fun postEvent() {
        viewModel.postEvent(activityViewModel.getIdempotencyKey())
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

    override fun onDestroyView() {
        super.onDestroyView()

        requireActivity().finish()
    }

    companion object {
        @JvmStatic
        fun newInstance() = RewardDialog()
    }
}
