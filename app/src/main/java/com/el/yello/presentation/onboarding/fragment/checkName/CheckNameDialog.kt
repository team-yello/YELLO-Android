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
import com.example.ui.extension.toast
import com.example.ui.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckNameDialog :
    BindingDialogFragment<FragmentDialogCheckNameBinding>(R.layout.fragment_dialog_check_name) {

    override fun onStart() {
        super.onStart()
        setDialogBackground()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditNameBtnListener()
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

    private fun initEditNameBtnListener() {
        val bundle = arguments
        if (bundle != null) {
            with(binding) {
                tvNameEditDialogTitleTwo.text = bundle.getString(EXTRA_NAME)
                btnNameEditDialogYes.setOnSingleClickListener {
                    Intent(requireContext(), OnBoardingActivity::class.java).apply {
                        putExtras(bundle)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(this)
                    }
                    dismiss()
                    requireActivity().finish()
                }
                btnNameEditDialogNo.setOnSingleClickListener {
                    Intent(requireContext(), EditNameActivity::class.java).apply {
                        putExtras(bundle)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(this)
                    }
                    dismiss()
                    requireActivity().finish()
                }
            }
        } else {
            toast(getString(R.string.internet_connection_error_msg))
        }
    }
}
