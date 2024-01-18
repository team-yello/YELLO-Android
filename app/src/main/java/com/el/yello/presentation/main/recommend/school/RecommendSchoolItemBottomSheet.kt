package com.el.yello.presentation.main.recommend.school

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendSchoolItemBottomSheetBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener

class RecommendSchoolItemBottomSheet :
    BindingBottomSheetDialog<FragmentRecommendSchoolItemBottomSheetBinding>(R.layout.fragment_recommend_school_item_bottom_sheet) {

    private val viewModel by activityViewModels<RecommendSchoolViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
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

    private fun initDeleteBtnListener() {
        binding.btnRecommendFriendDelete.setOnSingleClickListener {
            // TODO : 체크 표시 전환 후 dismiss ,
            //  viewModel.deleteFriendDataToServer
            dismiss()
        }
    }
}
