package com.el.yello.presentation.pay

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.el.yello.R
import com.el.yello.databinding.FragmentPaySubsDialogBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class PaySubsDialog :
    BindingDialogFragment<FragmentPaySubsDialogBinding>(R.layout.fragment_pay_subs_dialog) {

    override fun onStart() {
        super.onStart()
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

        initConfirmBtnListener()
    }

    private fun initConfirmBtnListener() {
        binding.btnPayConfirm.setOnSingleClickListener {
            dismiss()
        }
    }
}