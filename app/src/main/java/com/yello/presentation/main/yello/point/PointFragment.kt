package com.yello.presentation.main.yello.point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.type.YelloState.Wait
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentPointBinding
import com.yello.presentation.main.yello.YelloViewModel
import com.yello.presentation.main.yello.YelloViewModel.Companion.SEC_MAX_LOCK_TIME

class PointFragment : BindingFragment<FragmentPointBinding>(R.layout.fragment_point) {
    val viewModel by activityViewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setConfirmBtnClickListener()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnPointConfirm.setOnSingleClickListener {
            // TODO: 투표 생성하기 API 연결
            viewModel.setVoteState(Wait(SEC_MAX_LOCK_TIME))
            requireActivity().finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PointFragment()
    }
}
