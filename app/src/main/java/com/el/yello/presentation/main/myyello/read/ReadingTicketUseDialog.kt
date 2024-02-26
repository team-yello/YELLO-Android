package com.el.yello.presentation.main.myyello.read

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.DialogReadingTicketUseBinding
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.base.BindingDialogFragment
import com.example.ui.extension.setOnSingleClickListener

class ReadingTicketUseDialog :
    BindingDialogFragment<DialogReadingTicketUseBinding>(R.layout.dialog_reading_ticket_use) {
    private val viewModel by activityViewModels<MyYelloReadViewModel>()

    private var _isKeywordOpened: Boolean? = null
    private val isKeywordOpened
        get() = _isKeywordOpened ?: false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundleArgs()
        initView()
        setDialogBackground()
        initEvent()
    }

    private fun getBundleArgs() {
        arguments ?: return
        _isKeywordOpened = arguments?.getBoolean(ARGS_IS_KEYWORD_OPENED)
    }

    private fun initView() {
        binding.tvKey.text = viewModel.myReadingTicketCount.toString()
    }

    private fun initEvent() {
        binding.tvOk.setOnSingleClickListener {
            if (isKeywordOpened) {
                AmplitudeManager.trackEventWithProperties("click_modal_fullname_yes")
            } else {
                AmplitudeManager.trackEventWithProperties("click_modal_fullnamefirst_yes")
            }
            dismiss()
            ReadingTicketAfterDialog.newInstance().show(parentFragmentManager, "dialog")
        }

        binding.tvNo.setOnSingleClickListener {
            if (isKeywordOpened) {
                AmplitudeManager.trackEventWithProperties("click_modal_fullname_no")
            } else {
                AmplitudeManager.trackEventWithProperties("click_modal_fullnamefirst_no")
            }
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
            setBackgroundDrawableResource(R.drawable.shape_grayscales900_fill_12_rect)
        }
        dialog?.setCancelable(true)
    }

    companion object {
        private const val ARGS_IS_KEYWORD_OPENED = "is_keyword_opened"

        @JvmStatic
        fun newInstance(isKeywordOpened: Boolean) = ReadingTicketUseDialog().apply {
            arguments = bundleOf(
                ARGS_IS_KEYWORD_OPENED to isKeywordOpened,
            )
        }
    }
}
