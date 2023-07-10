package com.yello.presentation.main.recommend

import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.RecommendModel
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
                // TODO : 픽셀값 -> dp로 변환 필요
                iconPadding = -4
                setPadding(30)
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