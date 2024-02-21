package com.el.yello.presentation.main.profile.info

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentProfileItemBottomSheetBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.util.extension.setImageOrBasicThumbnail
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.extension.setOnSingleClickListener

class ProfileFriendItemBottomSheet :
    BindingBottomSheetDialog<FragmentProfileItemBottomSheetBinding>(R.layout.fragment_profile_item_bottom_sheet) {

    private var deleteBottomSheet: ProfileFriendDeleteBottomSheet? =
        ProfileFriendDeleteBottomSheet()
    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.isItemBottomSheetRunning = true
        setItemImage()
        initDeleteBtnListener()
    }

    private fun setItemImage() {
        binding.ivProfileFriendThumbnail.setImageOrBasicThumbnail(viewModel.clickedUserData.profileImageUrl)
    }

    private fun initDeleteBtnListener() {
        binding.btnProfileFriendDelete.setOnSingleClickListener {
            deleteBottomSheet?.show(parentFragmentManager, DELETE_BOTTOM_SHEET)
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.isItemBottomSheetRunning = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        deleteBottomSheet = null
    }

    private companion object {
        const val DELETE_BOTTOM_SHEET = "deleteBottomSheet"
    }
}
