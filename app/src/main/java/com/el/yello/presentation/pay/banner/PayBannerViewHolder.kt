package com.el.yello.presentation.pay.banner

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.el.yello.R
import com.el.yello.databinding.ItemPayBannerBinding

class PayBannerViewHolder(
    val binding: ItemPayBannerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(position: Int) {
        binding.ivPayBanner.load(
            when (position) {
                0 -> R.drawable.img_pay_banner_first
                1 -> R.drawable.img_pay_banner_second
                2 -> R.drawable.img_pay_banner_third
                else -> R.drawable.img_pay_banner_fourth
            }
        )
    }
}