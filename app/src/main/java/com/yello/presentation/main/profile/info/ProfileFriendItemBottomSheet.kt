package com.yello.presentation.main.profile.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileFriendItemBottomSheetBinding
import com.yello.presentation.main.profile.ProfileViewModel

class ProfileFriendItemBottomSheet :
    BindingBottomSheetDialog<FragmentProfileFriendItemBottomSheetBinding>(R.layout.fragment_profile_friend_item_bottom_sheet) {

    private val profileFriendDeleteBottomSheet: ProfileFriendDeleteBottomSheet =
        ProfileFriendDeleteBottomSheet()
    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initDeleteButton()
        setItemData()
    }

    private fun setItemData() {
        if (viewModel.clickedItemThumbnail.value != "") {
            binding.ivProfileFriendThumbnail.load(viewModel.clickedItemThumbnail.value) {
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun initDeleteButton() {
        binding.btnProfileFriendDelete.setOnSingleClickListener {
            dismiss()
            profileFriendDeleteBottomSheet.show(parentFragmentManager, "Dialog")
        }
    }
}