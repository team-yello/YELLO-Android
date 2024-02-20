package com.el.yello.presentation.pay.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemPayFirstBinding
import com.el.yello.databinding.ItemPayFourthBinding
import com.el.yello.databinding.ItemPaySecondBinding
import com.el.yello.databinding.ItemPayThirdBinding

class PayBannerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> BannerItem.ONE.ordinal
            1 -> BannerItem.TWO.ordinal
            2 -> BannerItem.THREE.ordinal
            else -> BannerItem.FOUR.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        return when (viewType) {
            BannerItem.ONE.ordinal -> {
                val binding = ItemPayFirstBinding.inflate(inflater, parent, false)
                PayOneViewHolder(binding)
            }

            BannerItem.TWO.ordinal -> {
                val binding = ItemPayThirdBinding.inflate(inflater, parent, false)
                PayThreeViewHolder(binding)
            }

            BannerItem.THREE.ordinal -> {
                val binding = ItemPaySecondBinding.inflate(inflater, parent, false)
                PayTwoViewHolder(binding)
            }

            else -> {
                val binding = ItemPayFourthBinding.inflate(inflater, parent, false)
                PayFourViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PayOneViewHolder -> holder.onBind()
            is PayTwoViewHolder -> holder.onBind()
            is PayThreeViewHolder -> holder.onBind()
            is PayFourViewHolder -> holder.onBind()
        }
    }

    override fun getItemCount(): Int = TOTAL_BANNER_COUNT

    companion object {
        const val TOTAL_BANNER_COUNT = 4
    }
}
