package com.el.yello.presentation.main.profile.univmod

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentProfileModDialogBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnivModDialog :
    BindingDialogFragment<FragmentProfileModDialogBinding>(R.layout.fragment_profile_mod_dialog) {

    private val viewModel by activityViewModels<UnivProfileModViewModel>()

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

        initModBtnListener()
        initRejectBtnListener()
    }

    private fun initRejectBtnListener() {
        binding.btnProfileDialogReject.setOnSingleClickListener { dismiss() }
    }

    private fun initModBtnListener() {
        binding.btnProfileDialogMod.setOnSingleClickListener {
            viewModel.postNewProfileToServer()
            dismiss()
        }
    }
}