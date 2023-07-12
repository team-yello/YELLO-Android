package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileQuitDialogBinding

class ProfileQuitDialog :
    BindingDialogFragment<FragmentProfileQuitDialogBinding>(R.layout.fragment_profile_quit_dialog) {


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
}