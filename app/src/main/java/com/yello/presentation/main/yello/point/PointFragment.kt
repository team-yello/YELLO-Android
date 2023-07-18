package com.yello.presentation.main.yello.point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentPointBinding
import com.yello.presentation.main.yello.YelloViewModel
import com.yello.presentation.main.yello.vote.VoteViewModel

class PointFragment : BindingFragment<FragmentPointBinding>(R.layout.fragment_point) {
    val viewModel by activityViewModels<YelloViewModel>()
    val voteViewModel by activityViewModels<VoteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = voteViewModel

        setConfirmBtnClickListener()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnPointConfirm.setOnSingleClickListener {
            viewModel.getVoteState()
            requireActivity().finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PointFragment()
    }
}
