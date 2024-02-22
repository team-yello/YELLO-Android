package com.el.yello.presentation.main.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.el.yello.databinding.ItemTimelineBinding
import com.example.domain.entity.LookListModel.LookModel
import com.example.ui.util.ItemDiffCallback

class TimelinePageAdapter : PagingDataAdapter<LookModel, TimelineViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        val binding: ItemTimelineBinding = ItemTimelineBinding.inflate(inflater, parent, false)
        return TimelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.binding.tvNameHead.visibility = View.VISIBLE
        holder.binding.tvKeywordHead.visibility = View.VISIBLE
        val item = getItem(position) ?: return
        holder.onBind(item)
    }

    companion object {
        private val diffUtil = ItemDiffCallback<LookModel>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}