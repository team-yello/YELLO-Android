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

        setConfirmBtnClickListener()
        observeCheckIsSubscribed()
        yelloViewModel.getPurchaseInfoFromServer()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnPointConfirmDouble.setOnSingleClickListener {
            requireActivity().finish()
        }
    }

    private fun observeCheckIsSubscribed() {
        yelloViewModel.getPurchaseInfoState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.tvPointPlusLabel.isVisible = state.data.isSubscribe
                    if (state.data.isSubscribe) {
                        binding.tvPointVotePoint.text =
                            voteViewModel.votePointSum.times(2).toString()
                    }
                }

                is UiState.Failure -> {
                    binding.tvPointPlusLabel.isVisible = false
                }

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

    companion object {
        @JvmStatic
        fun newInstance() = PointFragment()
    }
}
