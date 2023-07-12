package com.yello.presentation.main.profile

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileQuitDialogBinding

class ProfileQuitDialog :
    BindingDialogFragment<FragmentProfileQuitDialogBinding>(R.layout.fragment_profile_quit_dialog) {

    override fun onStart() {
        super.onStart()
        setDialogBackground()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfileDialogQuit.setOnSingleClickListener {
            // TODO: 회원 탈퇴 로직 설정
            // TODO: 스플래쉬 화면으로 이동
        }

        binding.btnProfileDialogReject.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 16) * 2

        dialog?.window?.apply {
            setLayout(
                (deviceWidth - dialogHorizontalMargin * 2).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
    }
}