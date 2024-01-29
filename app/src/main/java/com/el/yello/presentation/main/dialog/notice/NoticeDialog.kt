package com.el.yello.presentation.main.dialog.notice

import com.el.yello.R
import com.el.yello.databinding.FragmentNoticeDialogBinding
import com.example.ui.base.BindingDialogFragment

class NoticeDialog : BindingDialogFragment<FragmentNoticeDialogBinding>(R.layout.fragment_notice_dialog) {

    companion object {
        @JvmStatic
        fun newInstance() = NoticeDialog()
    }
}
