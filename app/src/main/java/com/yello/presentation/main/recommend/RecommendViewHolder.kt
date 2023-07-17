package com.yello.presentation.main.recommend

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.RecommendModel
import com.example.ui.view.setOnSingleClickListener
import com.yello.databinding.ItemRecommendListBinding

class RecommendViewHolder(
    val binding: ItemRecommendListBinding,
    private val itemClick: (RecommendModel) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: RecommendModel) {
        binding.tvRecommendItemName.text = item.name
        binding.tvRecommendItemSchool.text = item.group
        item.profileImage?.let { profileImage ->
            binding.ivRecommendItemThumbnail.load(profileImage) {
                transformations(CircleCropTransformation())
            }
        }

        binding.btnRecommendItemAdd.setOnSingleClickListener {
            itemClick(item)
        }
    }
}