package com.el.yello.presentation.main.recommend.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.el.yello.databinding.ItemRecommendSearchBinding
import com.example.domain.entity.RecommendSearchModel.SearchFriendModel
import com.example.ui.view.ItemDiffCallback

class RecommendSearchAdapter(private val itemClick: (SearchFriendModel, Int) -> Unit) :
    ListAdapter<SearchFriendModel, RecommendSearchViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendSearchViewHolder {
        val binding: ItemRecommendSearchBinding =
            ItemRecommendSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendSearchViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: RecommendSearchViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    companion object {
        private val diffUtil = ItemDiffCallback<SearchFriendModel>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}