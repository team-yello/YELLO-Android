package com.el.yello.presentation.onboarding.fragment.checkName

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogCheckNameBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_NAME
import com.el.yello.presentation.onboarding.activity.EditNameActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.example.ui.base.BindingDialogFragment
import com.example.ui.fragment.toast
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckNameDialog :
    BindingDialogFragment<FragmentDialogCheckNameBinding>(R.layout.fragment_dialog_check_name) {

    override fun onStart() {
        super.onStart()
        setDialogBackground()
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
        dialog?.setCancelable(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEditBtnListener()
    }

    private fun initEditBtnListener() {
        val bundle = arguments
        if (bundle != null) {
            // 다이얼로그에 카카오 계정 이름 띄우기
            binding.tvNameEditDialogTitleTwo.text = bundle.getString(EXTRA_NAME)

            // 이름 유지 (Y)
            binding.btnNameEditDialogYes.setOnSingleClickListener {
                Intent(requireContext(), OnBoardingActivity::class.java).apply {
                    putExtras(bundle)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(this)
                }
                dismiss()
                requireActivity().finish()
            }
            // 이름 수정 (N)
            binding.btnNameEditDialogNo.setOnSingleClickListener {
                Intent(requireContext(), EditNameActivity::class.java).apply {
                    putExtras(bundle)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(this)
                }
                dismiss()
                requireActivity().finish()
            }
        } else {
            toast(getString(R.string.sign_in_error_connection))
        }
    }

}
