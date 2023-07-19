package com.yello.presentation.main.recommend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.RecommendModel
import com.example.ui.intent.dpToPx
import com.yello.databinding.ItemRecommendListBinding


class RecommendAdapter(private val itemClick: (RecommendModel.RecommendFriend, Int, RecommendViewHolder) -> (Unit)) :
    RecyclerView.Adapter<RecommendViewHolder>() {

    private var itemList = mutableListOf<RecommendModel.RecommendFriend>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        val binding: ItemRecommendListBinding =
            ItemRecommendListBinding.inflate(inflater, parent, false)
        return RecommendViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.onBind(itemList[position], position)

        changeToTextButton(holder)
    }


    override fun getItemCount(): Int = itemList.size

    fun addItemList(newItems: List<RecommendModel.RecommendFriend>) {
        this.itemList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    // 초기 아이템 텍스트 버튼으로 설정
    private fun changeToTextButton(holder: RecommendViewHolder) {
        holder.binding.btnRecommendItemAdd.apply {
            text = "친구추가"
            icon = null
            setPadding(
                dpToPx(holder.binding.root.context, 13),
                0,
                dpToPx(holder.binding.root.context, 13),
                0
            )
        }
    }
}