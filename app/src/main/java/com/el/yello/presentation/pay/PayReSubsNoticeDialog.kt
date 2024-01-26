package com.el.yello.presentation.pay

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.el.yello.R
import com.el.yello.databinding.FragmentNoticeResubscribeBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class PayReSubsNoticeDialog :
    BindingDialogFragment<FragmentNoticeResubscribeBinding>(R.layout.fragment_notice_resubscribe) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNoticeBtnClickListener()
        getArgExpiredDate()
    }

    override fun onStart() {
        super.onStart()
        showDialogFullScreen()
    }

    private fun showDialogFullScreen() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun setNoticeBtnClickListener() {
        binding.btnNoticeQuit.setOnSingleClickListener {
            dismiss()
        }
        binding.btnYelloplusSubscribe.setOnSingleClickListener {
            Intent(requireContext(), PayActivity::class.java).apply {
                startActivity(this)
            }
            dismiss()
        }
    }

    private fun setExpiredDate(expiredDate: String) {
        if (isAdded) {
            binding.tvResubscribeExpiredDate.text = expiredDate
        }
    }

    private fun getArgExpiredDate() {
        val expiredDate = arguments?.getString(ARG_EXPIRED_DATE)
        expiredDate?.let {
            setExpiredDate(it)
        }
    }

    companion object {
        private const val ARG_EXPIRED_DATE = "arg_expired_date"

        @JvmStatic
        fun newInstance(expiredDate: String): PayReSubsNoticeDialog {
            return PayReSubsNoticeDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXPIRED_DATE, expiredDate)
                }
            }
        }
    }
}
