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
    val shopClick: (ProfileViewModel) -> (Unit),
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(viewModel: ProfileViewModel) {
        with(binding) {
            vm = viewModel
            ivSubsStar.isVisible = viewModel.isSubscribed
            ivSubsLine.isVisible = viewModel.isSubscribed

            btnProfileAddGroup.setOnSingleClickListener { buttonClick(viewModel) }
            btnProfileShop.setOnSingleClickListener { shopClick(viewModel) }
            btnProfileShopSale.setOnSingleClickListener { shopClick(viewModel) }

            ivProfileInfoThumbnail.apply {
                val thumbnail = viewModel.myUserData.profileImageUrl
                load(if (thumbnail == BASIC_THUMBNAIL) R.drawable.img_yello_basic else thumbnail) {
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}
