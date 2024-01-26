package com.el.yello.presentation.main.profile.info

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemProfileUserInfoBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.util.Utils.setImageOrBasicThumbnail
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

            ivProfileInfoThumbnail.setImageOrBasicThumbnail(viewModel.myUserData.profileImageUrl)
        }
    }
}
