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
        binding.btnPointConfirm.setOnSingleClickListener {
            yelloViewModel.getVoteState()
            requireActivity().finish()
        }
    }

    private fun observeCheckIsSubscribed() {
        yelloViewModel.getPurchaseInfoState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        binding.tvPointPlusLabel.isVisible = state.data.isSubscribe
                        if (state.data.isSubscribe) {
                            binding.tvPointVotePoint.text =
                                voteViewModel.votePointSum.times(2).toString()
                        }
                    }

                    is UiState.Failure -> {
                        binding.tvPointPlusLabel.visibility = View.GONE
                        binding.tvPointVotePoint.text = voteViewModel.votePointSum.toString()
                    }

                    is UiState.Loading -> {}

                    is UiState.Empty -> {}
                }
                binding.tvPointVotePoint.visibility = View.VISIBLE
                binding.tvPointVotePointPlus.visibility = View.VISIBLE
                binding.tvPointVotePointLabel.visibility = View.VISIBLE
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PointFragment()
    }
}
