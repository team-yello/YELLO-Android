package com.el.yello.presentation.pay.dialog

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.el.yello.R
import com.el.yello.databinding.FragmentPaySubsDialogBinding
import com.el.yello.presentation.pay.PayActivity
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class PaySubsDialog :
    BindingDialogFragment<FragmentPaySubsDialogBinding>(R.layout.fragment_pay_subs_dialog) {

    override fun onStart() {
        super.onStart()
        setDialogBackground()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initConfirmBtnListener()
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 16) * 2

        dialog?.window?.apply {
            setLayout(
                (deviceWidth - dialogHorizontalMargin * 2).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(true)
    }

    private fun initConfirmBtnListener() {
        binding.btnPayConfirm.setOnSingleClickListener {
            dismiss()
            (activity as PayActivity).finish()
        }
    }
}