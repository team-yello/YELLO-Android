package com.el.yello.presentation.main.dialog.notice

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentNoticeDialogBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class NoticeDialog :
    BindingDialogFragment<FragmentNoticeDialogBinding>(R.layout.fragment_notice_dialog) {
    private val viewModel by viewModels<NoticeViewModel>()

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

        initDoNotSeeItAgainBtnClickListener()
        initCloseBtnClickListener()
    }

    private fun initDoNotSeeItAgainBtnClickListener() {
        binding.btnNoticeDoNotSeeItAgain.setOnSingleClickListener {
            switchDoNotSeeItAgainState()
        }
        binding.icNoticeDoNotSeeItAgain.setOnSingleClickListener {
            switchDoNotSeeItAgainState()
        }
        binding.tvNoticeDoNotSeeItAgain.setOnSingleClickListener {
            switchDoNotSeeItAgainState()
        }
    }

    private fun switchDoNotSeeItAgainState() {
        val isBtnDisabled = viewModel.switchNoticeDisabledState()
        binding.icNoticeDoNotSeeItAgain.setImageResource(
            if (isBtnDisabled) R.drawable.ic_notice_uncheck else R.drawable.ic_notice_check,
        )
    }

    private fun initCloseBtnClickListener() {
        binding.tvNoticeClose.setOnSingleClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // TODO : 로컬 디비에 초기 1회 다시 보지 않기 선택 여부 저장
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoticeDialog()
    }
}
