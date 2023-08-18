package com.el.yello.presentation.pay

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import coil.load
import com.el.yello.R
import com.el.yello.databinding.FragmentPayInAppDialogBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class PayInAppDialog :
    BindingDialogFragment<FragmentPayInAppDialogBinding>(R.layout.fragment_pay_in_app_dialog) {

    private val viewModel by activityViewModels<PayViewModel>()

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
        setDialogByItem()
    }

    private fun initConfirmBtnListener() {
        binding.btnPayConfirm.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun setDialogByItem() {
        when (viewModel.currentInAppItem) {
            1 -> {
                binding.tvPayDialogSubtitle1.text = getString(R.string.pay_dialog_in_app_title_1)
                binding.ivPayInApp.load(R.drawable.ic_pay_in_app_1)
            }

            2 -> {
                binding.tvPayDialogSubtitle1.text = getString(R.string.pay_dialog_in_app_title_2)
                binding.ivPayInApp.load(R.drawable.ic_pay_in_app_2)
            }

            5 -> {
                binding.tvPayDialogSubtitle1.text = getString(R.string.pay_dialog_in_app_title_5)
                binding.ivPayInApp.load(R.drawable.ic_pay_in_app_5)
            }

            else -> dismiss()
        }
    }
}