package com.el.yello.presentation.main.yello.point

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentPointBinding
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.vote.VoteViewModel
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PointFragment : BindingFragment<FragmentPointBinding>(R.layout.fragment_point) {

    private val yelloViewModel by activityViewModels<YelloViewModel>()
    private val voteViewModel by activityViewModels<VoteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = voteViewModel
        yelloViewModel.getPurchaseInfoFromServer()
        setConfirmBtnClickListener()
        observeCheckIsSubscribed()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnPointConfirmDouble.setOnSingleClickListener {
            requireActivity().finish()
        }
        binding.btnPointJustConfirm.setOnSingleClickListener {
            requireActivity().finish()
        }
    }

    private fun observeCheckIsSubscribed() {
        yelloViewModel.getPurchaseInfoState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    showDoubleConfirm(state.data.isSubscribe)
                    binding.tvPointPlusLabel.isVisible = state.data.isSubscribe
                }

                is UiState.Failure -> showDoubleConfirm(false)

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }

            with(binding) {
                tvPointVotePoint.isVisible = true
                tvPointVotePointPlus.isVisible = true
                tvPointVotePointLabel.isVisible = true
            }
        }.launchIn(lifecycleScope)
    }

    private fun showDoubleConfirm(isDouble: Boolean) {
        with(binding) {
            btnPointJustConfirm.isVisible = !isDouble
            btnPointAdToDouble.isVisible = !isDouble
            btnPointConfirmDouble.isVisible = isDouble
        }
        if (isDouble) {
            binding.tvPointVotePoint.text = voteViewModel.votePointSum.times(2).toString()
        }
    }



    companion object {
        @JvmStatic
        fun newInstance() = PointFragment()
    }
}
