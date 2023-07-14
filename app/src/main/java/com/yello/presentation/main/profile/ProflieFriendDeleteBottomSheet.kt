package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.fragment.toast
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileFriendDeleteBottomSheetBinding

class ProflieFriendDeleteBottomSheet :
    BindingBottomSheetDialog<FragmentProfileFriendDeleteBottomSheetBinding>(R.layout.fragment_profile_friend_delete_bottom_sheet) {

    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setItemData()
        initReturnButton()
        initDeleteButton()
    }

    private fun setItemData() {
        binding.tvProfileFriendDeleteName.text = viewModel.clickedItemName.value
        binding.tvProfileFriendDeleteId.text = viewModel.clickedItemId.value
        binding.tvProfileFriendDeleteSchool.text = viewModel.clickedItemSchool.value
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
            // TODO: 친구 삭제 로직 구현
            toast("${viewModel.clickedItemName.value} 님과 친구 끊기를 완료했어요.")
            dismiss()
        }
    }
}