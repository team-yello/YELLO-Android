package com.el.yello.presentation.pay.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemPayBannerBinding

class PayBannerAdapter : RecyclerView.Adapter<PayBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayBannerViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        val binding = ItemPayBannerBinding.inflate(inflater, parent, false)
        return PayBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PayBannerViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = TOTAL_BANNER_COUNT

    companion object {
        const val TOTAL_BANNER_COUNT = 4
    }
}
