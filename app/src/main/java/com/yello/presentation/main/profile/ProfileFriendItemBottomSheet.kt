package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileFriendItemBottomSheetBinding

class ProfileFriendItemBottomSheet :
    BindingBottomSheetDialog<FragmentProfileFriendItemBottomSheetBinding>(R.layout.fragment_profile_friend_item_bottom_sheet) {

    private val profileFriendDeleteBottomSheet: ProflieFriendDeleteBottomSheet =
        ProflieFriendDeleteBottomSheet()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDeleteButton()
    }

    private fun initDeleteButton() {
        binding.btnProfileFriendDelete.setOnSingleClickListener {
            dismiss()
            profileFriendDeleteBottomSheet.show(parentFragmentManager, "Dialog")
        }
    }
}