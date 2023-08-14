package com.el.yello.presentation.main.recommend.list

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.databinding.ItemRecommendListBinding
import com.example.domain.entity.RecommendModel.RecommendFriend
import com.example.ui.view.setOnSingleClickListener

class RecommendViewHolder(
    val binding: ItemRecommendListBinding,
    private val itemClick: (RecommendFriend, Int, RecommendViewHolder) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: RecommendFriend, position: Int) {
        binding.tvRecommendItemName.text = item.name
        binding.tvRecommendItemSchool.text = item.group
        item.profileImage?.let { profileImage ->
            binding.ivRecommendItemThumbnail.load(profileImage) {
                transformations(CircleCropTransformation())
            }
        }

        binding.btnRecommendItemAdd.setOnSingleClickListener {
            itemClick(item, position, this)
        }
    }
}
