package com.el.yello.presentation.pay.dialog

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentPayPointDialogBinding
import com.el.yello.presentation.pay.PayViewModel
import com.el.yello.util.Image.loadUrl
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayPointDialog :
    BindingDialogFragment<FragmentPayPointDialogBinding>(R.layout.fragment_pay_point_dialog) {

    private val viewModel by viewModels<PayViewModel>()

    override fun onStart() {
        super.onStart()

        setupPostEventState()
        initContainerLayoutTransparent()
    }

    private fun setupPostEventState() {
        binding.tvRewardTitle.text = viewModel.rewardAdModel.rewardTitle
        binding.ivReward.loadUrl(viewModel.rewardAdModel.rewardImage)
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
}
