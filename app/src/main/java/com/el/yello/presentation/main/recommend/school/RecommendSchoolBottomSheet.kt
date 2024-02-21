package com.el.yello.presentation.main.recommend.school

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendSchoolItemBottomSheetBinding
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.extension.setOnSingleClickListener
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
        binding.ivRecommendFriendThumbnail.setImageOrBasicThumbnail(viewModel.clickedUserData.profileImageUrl)
    }

    private fun initAddBtnListener() {
        binding.btnRecommendFriendAdd.setOnSingleClickListener {
            binding.btnRecommendFriendAdd.isVisible = false
            binding.btnRecommendItemAddPressed.isVisible = true
            lifecycleScope.launch {
                viewModel.addFriendToServer(viewModel.clickedUserData.userId)
                delay(300)
                dismiss()
            }
        }
    }
}
