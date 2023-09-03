package com.el.yello.presentation.main.myyello.read

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.DialogReadingTicketUseBinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class ReadingTicketUseDialog : BindingDialogFragment<DialogReadingTicketUseBinding>(R.layout.dialog_reading_ticket_use) {
    private val viewModel by activityViewModels<MyYelloReadViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setDialogBackground()
        initEvent()
    }

    private fun initView() {
        binding.tvKey.text = viewModel.myReadingTicketCount.toString()
    }

    private fun initEvent() {
        binding.tvOk.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_modal_fullname_yes")
            dismiss()
            ReadingTicketAfterDialog.newInstance().show(parentFragmentManager, "dialog")
        }

        binding.tvNo.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_modal_fullname_no")
            dismiss()
        }
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 16) * 2

        dialog?.window?.apply {
            setLayout(
                (deviceWidth - dialogHorizontalMargin * 2).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.drawable.shape_fill_gray900_12dp)
        }
        dialog?.setCancelable(true)
    }
}
