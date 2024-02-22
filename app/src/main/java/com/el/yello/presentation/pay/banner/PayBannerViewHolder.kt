package com.el.yello.presentation.pay.banner

import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemPayFirstBinding
import com.el.yello.databinding.ItemPayFourthBinding
import com.el.yello.databinding.ItemPaySecondBinding
import com.el.yello.databinding.ItemPayThirdBinding

class PayOneViewHolder(
    binding: ItemPayFirstBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind() {}
}

class PayTwoViewHolder(
    binding: ItemPaySecondBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind() {}
}

class PayThreeViewHolder(
    binding: ItemPayThirdBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind() {}
}

class PayFourViewHolder(
    binding: ItemPayFourthBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind() {}
}