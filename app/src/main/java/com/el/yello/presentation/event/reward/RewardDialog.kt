package com.el.yello.presentation.event.reward

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.el.yello.R
import com.el.yello.databinding.FragmentRewardDialogBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class RewardDialog :
    BindingDialogFragment<FragmentRewardDialogBinding>(R.layout.fragment_reward_dialog) {

    override fun onStart() {
        super.onStart()

        initContainerLayoutTransparent()
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

    companion object {
        @JvmStatic
        fun newInstance() = RewardDialog()
    }
}
