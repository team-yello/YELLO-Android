package com.el.yello.presentation.main.yello.point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentPointBinding
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.vote.VoteViewModel
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

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
