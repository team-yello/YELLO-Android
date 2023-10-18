package com.el.yello.presentation.main.profile.info

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.ItemProfileUserInfoBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.presentation.main.profile.ProfileViewModel.Companion.BASIC_THUMBNAIL
import com.example.ui.view.setOnSingleClickListener

class ProfileUserInfoViewHolder(
    val binding: ItemProfileUserInfoBinding,
    val buttonClick: (ProfileViewModel) -> (Unit),
    val shopClick: (ProfileViewModel) -> (Unit)
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(viewModel: ProfileViewModel) {
        binding.vm = viewModel
        binding.ivSubsStar.isVisible = viewModel.isSubscribed
        binding.ivSubsLine.isVisible = viewModel.isSubscribed
        binding.btnProfileAddGroup.setOnSingleClickListener { buttonClick(viewModel) }
        binding.btnProfileShop.setOnSingleClickListener { shopClick(viewModel) }

        if (viewModel.myUserData.profileImageUrl == BASIC_THUMBNAIL) {
            binding.ivProfileInfoThumbnail.load(R.drawable.img_yello_basic)
        } else {
            binding.ivProfileInfoThumbnail.load(viewModel.myUserData.profileImageUrl) {
                transformations(CircleCropTransformation())
            }
        }
    }
}
