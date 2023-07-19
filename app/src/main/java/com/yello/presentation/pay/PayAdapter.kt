package com.yello.presentation.pay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yello.databinding.ItemPayFirstBinding
import com.yello.databinding.ItemPaySecondBinding
import com.yello.databinding.ItemPayThirdBinding

class PayAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var monthText: String = ""
    private var passMoneyBenefitPointText: String = ""
    private var turnPassMoneyBenefitMode: Boolean = false

    enum class Type {
        ONE,
        TWO,
        THREE
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> Type.ONE.ordinal
            1 -> Type.TWO.ordinal
            else -> Type.THREE.ordinal
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Type.ONE.ordinal -> {
                val binding = ItemPayFirstBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PayOneViewHolder(binding)
            }

            Type.TWO.ordinal -> {
                val binding = ItemPaySecondBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PayTwoViewHolder(binding)
            }

            else -> {
                val binding = ItemPayThirdBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PayThreeViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PayOneViewHolder -> holder.onBind()
            is PayTwoViewHolder -> holder.onBind()
            is PayThreeViewHolder -> holder.onBind()
        }
    }

    override fun getItemCount(): Int = 3
}

class PayOneViewHolder(
    private val binding: ItemPayFirstBinding
): RecyclerView.ViewHolder(binding.root) {
    fun onBind() {

    }

}

class PayTwoViewHolder(
    private val binding: ItemPaySecondBinding
): RecyclerView.ViewHolder(binding.root) {
    fun onBind() {

    }
}

class PayThreeViewHolder(
    private val binding: ItemPayThirdBinding
): RecyclerView.ViewHolder(binding.root) {
    fun onBind() {

    }
}
