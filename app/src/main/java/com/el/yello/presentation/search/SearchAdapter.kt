package com.el.yello.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.el.yello.databinding.ItemRecommendSearchBinding
import com.example.domain.entity.SearchListModel.SearchFriendModel
import com.example.ui.util.ItemDiffCallback

class SearchAdapter(
    private val itemClick: (SearchFriendModel, Int, SearchViewHolder) -> Unit
) : ListAdapter<SearchFriendModel, SearchViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        val binding: ItemRecommendSearchBinding =
            ItemRecommendSearchBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.onBind(item, position)
    }

    fun addList(newItems: List<SearchFriendModel>) {
        val currentItems = currentList.toMutableList()
        currentItems.addAll(newItems)
        submitList(currentItems)
    }

    companion object {
        private val diffUtil = ItemDiffCallback<SearchFriendModel>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}