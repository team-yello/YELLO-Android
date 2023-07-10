package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentRecommendInviteDialogBinding

class RecommendInviteDialog :
    BindingDialogFragment<FragmentRecommendInviteDialogBinding>(R.layout.fragment_recommend_invite_dialog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnInviteDialogExit.setOnSingleClickListener {
            dismiss()
        }

        binding.btnInviteKakao.setOnSingleClickListener {

        }

        binding.btnInviteLink.setOnSingleClickListener {

        }
    }
}