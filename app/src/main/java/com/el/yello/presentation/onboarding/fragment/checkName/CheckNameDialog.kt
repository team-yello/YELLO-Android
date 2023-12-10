package com.el.yello.presentation.onboarding.fragment.checkName

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogCheckNameBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.presentation.onboarding.activity.EditNameActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener

class CheckNameDialog :
    BindingDialogFragment<FragmentDialogCheckNameBinding>(R.layout.fragment_dialog_check_name) {

    private val viewModel by activityViewModels<ProfileViewModel>()

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
        dialog?.setCancelable(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditBtnListener()
    }

    private fun initEditBtnListener() {
        binding.btnNameEditDialogYes.setOnSingleClickListener {
            val intent = Intent(requireContext(), OnBoardingActivity::class.java)
            startActivity(intent)
        }
        binding.btnNameEditDialogNo.setOnSingleClickListener {
            val intent = Intent(requireContext(), EditNameActivity::class.java)
            startActivity(intent)
        }
    }
}
