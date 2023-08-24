package com.el.yello.presentation.main.profile.info

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.FragmentProfileItemBottomSheetBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.presentation.main.profile.ProfileViewModel.Companion.BASIC_THUMBNAIL
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener

class ProfileFriendItemBottomSheet :
    BindingBottomSheetDialog<FragmentProfileItemBottomSheetBinding>(R.layout.fragment_profile_item_bottom_sheet) {

    private var profileFriendDeleteBottomSheet: ProfileFriendDeleteBottomSheet? =
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
        initDeleteBtnListener()
        setItemImage()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.isItemBottomSheetRunning = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileFriendDeleteBottomSheet = null
    }

    private fun setItemImage() {
        if (viewModel.clickedItemThumbnail.value == BASIC_THUMBNAIL) {
            binding.ivProfileFriendThumbnail.setImageResource(R.drawable.img_yello_basic)
        } else {
            binding.ivProfileFriendThumbnail.load(viewModel.clickedItemThumbnail.value) {
                transformations(CircleCropTransformation())
            }
        }
    }

    // 다음 바텀시트 출력
    private fun initDeleteBtnListener() {
        binding.btnProfileFriendDelete.setOnSingleClickListener {
            profileFriendDeleteBottomSheet?.show(parentFragmentManager, DELETE_BOTTOM_SHEET)
            dismiss()
        }
    }

    private companion object {
        const val DELETE_BOTTOM_SHEET = "deleteBottomSheet"
    }
}
