package com.el.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendItemBottomSheetBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.presentation.main.recommend.kakao.RecommendViewModel
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecommendItemBottomSheet :
    BindingBottomSheetDialog<FragmentRecommendItemBottomSheetBinding>(R.layout.fragment_recommend_item_bottom_sheet) {

    private val viewModel by activityViewModels<RecommendViewModel>()

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
            // viewModel.isAddFriendBtnClicked = true
            binding.btnRecommendFriendAdd.visibility = View.INVISIBLE
            binding.btnRecommendItemAddPressed.visibility = View.VISIBLE
            lifecycleScope.launch {
                delay(500)
                dismiss()
            }
        }
    }
}
