package com.el.yello.presentation.pay.dialog

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import coil.load
import com.el.yello.R
import com.el.yello.databinding.FragmentPayInAppDialogBinding
import com.el.yello.presentation.pay.PayActivity
import com.el.yello.presentation.pay.PayActivity.Companion.YELLO_FIVE
import com.el.yello.presentation.pay.PayActivity.Companion.YELLO_ONE
import com.el.yello.presentation.pay.PayActivity.Companion.YELLO_TWO
import com.el.yello.presentation.pay.PayViewModel
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class PayInAppDialog :
    BindingDialogFragment<FragmentPayInAppDialogBinding>(R.layout.fragment_pay_in_app_dialog) {

    private val viewModel by activityViewModels<PayViewModel>()

    override fun onStart() {
        super.onStart()
        setDialogBackground()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initConfirmBtnListener()
        setDialogByItem()
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

    private fun setDialogByItem() {
        when (viewModel.currentInAppItem) {
           YELLO_ONE -> {
                binding.tvPayDialogSubtitle1.text = getString(R.string.pay_dialog_in_app_title_1)
                binding.ivPayInApp.load(R.drawable.ic_pay_in_app_1)
            }

            YELLO_TWO -> {
                binding.tvPayDialogSubtitle1.text = getString(R.string.pay_dialog_in_app_title_2)
                binding.ivPayInApp.load(R.drawable.ic_pay_in_app_2)
            }

            YELLO_FIVE -> {
                binding.tvPayDialogSubtitle1.text = getString(R.string.pay_dialog_in_app_title_5)
                binding.ivPayInApp.load(R.drawable.ic_pay_in_app_5)
            }

            else -> dismiss()
        }
    }
}