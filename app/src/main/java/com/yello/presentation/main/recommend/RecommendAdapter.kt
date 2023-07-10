package com.yello.presentation.main.recommend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.RecommendModel
import com.example.ui.intent.dpToPx
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ItemRecommendListBinding
import kotlinx.coroutines.delay


class RecommendAdapter(context: Context) : RecyclerView.Adapter<RecommendViewHolder>() {

    private val inflater by lazy { LayoutInflater.from(context)}
    private var itemList = mutableListOf<RecommendModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val binding: ItemRecommendListBinding =
            ItemRecommendListBinding.inflate(inflater, parent, false)
        return RecommendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.onBind(itemList[position])

        holder.binding.btnRecommendItemAdd.setOnSingleClickListener {
            changeToCheckIcon(holder)
            removeItem(position)
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun setItemList(itemList: List<RecommendModel>) {
        this.itemList = itemList.toMutableList()
        notifyDataSetChanged()
    }

    private fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    private fun changeToCheckIcon(holder: RecommendViewHolder) {
        holder.binding.btnRecommendItemAdd.apply {
            text = null
            setIconResource(R.drawable.ic_check)
            setIconTintResource(R.color.black)
            iconPadding = dpToPx(holder.binding.root.context, -2)
            setPadding(dpToPx(holder.binding.root.context, 10))
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<RecommendModel>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}