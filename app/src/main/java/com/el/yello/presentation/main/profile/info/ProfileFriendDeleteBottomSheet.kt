package com.el.yello.presentation.main.profile.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.FragmentProfileDeleteBottomSheetBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener

class ProfileFriendDeleteBottomSheet :
    BindingBottomSheetDialog<FragmentProfileDeleteBottomSheetBinding>(R.layout.fragment_profile_delete_bottom_sheet) {

    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        setItemImage()
        initReturnBtnListener()
        initDeleteBtnListener()
        observeFriendDeleteState()
    }

    private fun setItemImage() {
        if (viewModel.clickedItemThumbnail.value == ProfileViewModel.BASIC_THUMBNAIL) {
            binding.ivProfileFriendDeleteThumbnail.load(R.drawable.img_yello_basic)
        } else {
            binding.ivProfileFriendDeleteThumbnail.load(viewModel.clickedItemThumbnail.value) {
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun initReturnBtnListener() {
        binding.btnProfileFriendDeleteReturn.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initDeleteBtnListener() {
        binding.btnProfileFriendDeleteResume.setOnSingleClickListener {
            viewModel.clickedItemId.value?.let { friendId ->
                viewModel.deleteFriendDataToServer(friendId)
            }
        }
    }

    // 친구 삭제 서버 통신 성공 시 토스트 띄우고 바텀시트 종료
    private fun observeFriendDeleteState() {
        viewModel.deleteFriendState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    toast(
                        getString(
                            R.string.profile_delete_bottom_sheet_toast,
                            viewModel.clickedItemName.value.toString()
                        )
                    )
                    viewModel.setDeleteFriendStateEmpty()
                    this@ProfileFriendDeleteBottomSheet.dismiss()
                }

                is UiState.Failure -> {
                    toast(getString(R.string.profile_error_delete_friend))
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }
}
