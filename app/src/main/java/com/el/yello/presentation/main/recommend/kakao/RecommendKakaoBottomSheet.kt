package com.el.yello.presentation.main.recommend.kakao

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendKakaoItemBottomSheetBinding
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.extension.setOnSingleClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecommendKakaoBottomSheet :
    BindingBottomSheetDialog<FragmentRecommendKakaoItemBottomSheetBinding>(R.layout.fragment_recommend_kakao_item_bottom_sheet) {

    private val viewModel by activityViewModels<RecommendKakaoViewModel>()

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
