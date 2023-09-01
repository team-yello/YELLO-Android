package com.el.yello.presentation.main.recommend.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemRecommendListBinding
import com.example.domain.entity.RecommendModel.RecommendFriend

class RecommendAdapter(
    private val itemClick: (RecommendFriend, Int, RecommendViewHolder) -> (Unit)
) :
    RecyclerView.Adapter<RecommendViewHolder>() {

    private var itemList = mutableListOf<RecommendFriend>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        val binding: ItemRecommendListBinding =
            ItemRecommendListBinding.inflate(inflater, parent, false)
        return RecommendViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        changeToTextButton(holder)
        holder.onBind(itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size

    fun addItemList(newItems: List<RecommendFriend>) {
        this.itemList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.itemList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    // 초기 아이템 텍스트 버튼으로 설정
    private fun changeToTextButton(holder: RecommendViewHolder) {
        holder.binding.btnRecommendItemAdd.visibility = View.VISIBLE
        holder.binding.btnRecommendItemAddPressed.visibility = View.GONE
    }
}
