package com.el.yello.presentation.main.profile.info

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemProfileUserInfoBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.example.ui.view.setOnSingleClickListener

class ProfileUserInfoViewHolder(
    val binding: ItemProfileUserInfoBinding,
    val shopClick: (Unit) -> (Unit),
    val modClick: (Unit) -> (Unit),
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(viewModel: ProfileViewModel) {
        with(binding) {
            vm = viewModel
            ivSubsStar.isVisible = viewModel.isSubscribed
            ivSubsLine.isVisible = viewModel.isSubscribed

            btnProfileShop.setOnSingleClickListener { shopClick(Unit) }
            btnProfileShopSale.setOnSingleClickListener { shopClick(Unit) }

            btnPrifileMod.setOnSingleClickListener { modClick(Unit) }

            ivProfileInfoThumbnail.setImageOrBasicThumbnail(viewModel.myUserData.profileImageUrl)
        }
    }
}
