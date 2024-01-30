package com.el.yello.presentation.main.dialog.notice

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.el.yello.R
import com.el.yello.databinding.FragmentNoticeDialogBinding
import com.example.ui.base.BindingDialogFragment

class NoticeDialog :
    BindingDialogFragment<FragmentNoticeDialogBinding>(R.layout.fragment_notice_dialog) {

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

        // TODO : 동적으로 공지 이미지 비율에 맞는 minHeight 지정
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoticeDialog()
    }
}
