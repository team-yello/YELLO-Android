package com.el.yello.presentation.main.recommend.list

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendItemBottomSheetBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener

class RecommendFriendItemBottomSheet :
    BindingBottomSheetDialog<FragmentRecommendItemBottomSheetBinding>(R.layout.fragment_recommend_item_bottom_sheet) {

    private var deleteBottomSheet: RecommendFriendItemBottomSheet? =
        RecommendFriendItemBottomSheet()
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
        if (viewModel.clickedUserData.profileImageUrl == ProfileViewModel.BASIC_THUMBNAIL) {
            binding.ivRecommendFriendThumbnail.load(R.drawable.img_yello_basic)
        } else {
            binding.ivRecommendFriendThumbnail.load(viewModel.clickedUserData.profileImageUrl) {
                transformations(CircleCropTransformation())
            }
        }
    }

    // 다음 바텀시트 출력
    private fun initDeleteBtnListener() {
        binding.btnRecommendFriendDelete.setOnSingleClickListener {
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
