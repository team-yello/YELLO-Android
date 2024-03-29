package com.el.yello.presentation.main.profile.info

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemProfileUserInfoBinding
import com.el.yello.util.extension.loadUrl
import com.el.yello.util.extension.setImageOrBasicThumbnail
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.extension.setOnSingleClickListener

class ProfileUserInfoViewHolder(
    val binding: ItemProfileUserInfoBinding,
    private val shopClick: (Unit) -> (Unit),
    private val modClick: (Unit) -> (Unit),
    private val bannerClick: (String) -> (Unit),
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(viewModel: ProfileViewModel) {
        with(binding) {
            vm = viewModel
            ivSubsStar.isVisible = viewModel.isSubscribed
            ivSubsLine.isVisible = viewModel.isSubscribed

            btnProfileShop.setOnSingleClickListener { shopClick(Unit) }
            btnProfileShopSale.setOnSingleClickListener { shopClick(Unit) }

            layoutProfileInfo.setOnSingleClickListener { modClick(Unit) }

            ivProfileInfoThumbnail.setImageOrBasicThumbnail(viewModel.myUserData.profileImageUrl)

            ivProfileBanner.isVisible = viewModel.profileBanner.isAvailable
        }

        if (viewModel.profileBanner.isAvailable) {
            binding.ivProfileBanner.loadUrl(viewModel.profileBanner.imageUrl)
            binding.ivProfileBanner.setOnSingleClickListener {
                AmplitudeManager.trackEventWithProperties(EVENT_CLICK_PROFILE_BANNER)
                bannerClick(viewModel.profileBanner.redirectUrl)
            }
        }
    }

    companion object {
        private const val EVENT_CLICK_PROFILE_BANNER = "click_profile_banner"
    }
}
