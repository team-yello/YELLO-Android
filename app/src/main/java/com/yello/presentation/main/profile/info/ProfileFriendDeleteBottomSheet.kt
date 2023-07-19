package com.yello.presentation.main.profile.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileFriendDeleteBottomSheetBinding
import com.yello.presentation.main.profile.ProfileViewModel

class ProfileFriendDeleteBottomSheet :
    BindingBottomSheetDialog<FragmentProfileFriendDeleteBottomSheetBinding>(R.layout.fragment_profile_friend_delete_bottom_sheet) {

    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initReturnButton()
        initDeleteButton()
        observeFriendDeleteState()
        setItemData()
    }

    private fun setItemData() {
        if (viewModel.clickedItemThumbnail.value != "") {
            binding.ivProfileFriendDeleteThumbnail.load(viewModel.clickedItemThumbnail.value) {
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun initReturnButton() {
        binding.btnProfileFriendDeleteReturn.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initDeleteButton() {
        binding.btnProfileFriendDeleteResume.setOnSingleClickListener {
            viewModel.clickedItemId.value?.let { friendId ->
                viewModel.deleteFriendDataToServer(
                    friendId
                )
            }
        }
    }

    // 친구 삭제 서버 통신 성공 시 토스트 띄우고 바텀시트 종료
    private fun observeFriendDeleteState() {
        viewModel.deleteFriendState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    toast("${viewModel.clickedItemName.value} 님과 친구 끊기를 완료했어요.")
                    dismiss()
                    viewModel._deleteFriendState.value = UiState.Empty
                }

                is UiState.Failure -> {
                    toast("친구 삭제 서버 통신 실패")
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }
}