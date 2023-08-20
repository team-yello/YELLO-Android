package com.el.yello.presentation.main.yello.point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentPointBinding
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.vote.VoteViewModel
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener

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

    // 구독 여부 확인
    private fun observeCheckIsSubscribed() {
        yelloViewModel.getPurchaseInfoState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data?.isSubscribe == true) {
                        binding.tvPointPointLabel.visibility = View.VISIBLE
                    } else {
                        binding.tvPointPointLabel.visibility = View.GONE
                    }
                }

                is UiState.Failure -> {
                    binding.tvPointPointLabel.visibility = View.GONE
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PointFragment()
    }
}
