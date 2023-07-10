package com.yello.presentation.main.recommend

import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.RecommendModel
import com.example.ui.intent.dpToPx
import com.yello.R
import com.yello.databinding.ItemRecommendListBinding

class RecommendViewHolder(val binding: ItemRecommendListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRecommendItemAdd.setOnClickListener {
            binding.btnRecommendItemAdd.apply {
                text = null
                setIconResource(R.drawable.ic_check)
                setIconTintResource(R.color.black)
                iconPadding = dpToPx(binding.root.context, -2)
                setPadding(dpToPx(binding.root.context, 10))
            }
        }
    }

    fun onBind(item: RecommendModel) {
        binding.tvRecommendItemName.text = item.name
        binding.tvRecommendItemSchool.text = item.school
        if (item.thumbnail != null) {
            binding.ivRecommendItemThumbnail.load(item.thumbnail) {
                transformations(CircleCropTransformation())
            }
        }
    }
}