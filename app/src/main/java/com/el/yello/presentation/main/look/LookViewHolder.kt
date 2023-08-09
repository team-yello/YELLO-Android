package com.el.yello.presentation.main.look

import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemLookBinding
import com.example.domain.entity.LookListModel.LookModel

class LookViewHolder(
    val binding: ItemLookBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: LookModel) {
        binding.tvLookName.text = item.receiveName
        binding.tvLookTime.text = item.createdAt
        binding.tvNameHead.text = item.vote.nameHead
        binding.tvNameFoot.text = item.vote.nameFoot
        binding.tvKeywordHead.text = item.vote.keywordHead
        binding.tvKeyword.text = item.vote.keyword
        binding.tvKeywordFoot.text = item.vote.keywordFoot
    }

}