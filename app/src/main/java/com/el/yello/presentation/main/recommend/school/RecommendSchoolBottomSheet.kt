package com.el.yello.presentation.main.recommend.school

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendSchoolItemBottomSheetBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecommendSchoolBottomSheet :
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
        initAddBtnListener()
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

    private fun initAddBtnListener() {
        binding.btnRecommendFriendAdd.setOnSingleClickListener {
            binding.btnRecommendFriendAdd.visibility = View.INVISIBLE
            binding.btnRecommendItemAddPressed.visibility = View.VISIBLE
            lifecycleScope.launch {
                viewModel.addFriendToServer(viewModel.clickedUserData.userId)
                delay(300)
                dismiss()
            }
        }
    }
}
