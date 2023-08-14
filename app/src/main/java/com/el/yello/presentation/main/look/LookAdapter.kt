package com.el.yello.presentation.main.look

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.el.yello.databinding.ItemLookBinding
import com.example.domain.entity.LookListModel.LookModel
import com.example.ui.view.ItemDiffCallback

class LookAdapter :
    PagingDataAdapter<LookModel, LookViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LookViewHolder {
        val binding: ItemLookBinding =
            ItemLookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LookViewHolder, position: Int) {
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