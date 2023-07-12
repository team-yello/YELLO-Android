package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileFriendDeleteBottomSheetBinding

class ProflieFriendDeleteBottomSheet :
    BindingBottomSheetDialog<FragmentProfileFriendDeleteBottomSheetBinding>(R.layout.fragment_profile_friend_delete_bottom_sheet) {

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initReturnButton()
        initDeleteButton()
    }
    private fun initReturnButton() {
        binding.btnProfileFriendDeleteReturn.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initDeleteButton() {
        binding.btnProfileFriendDeleteResume.setOnSingleClickListener {
            // TODO: 친구 삭제 로직 구현
            dismiss()
        }
    }
}