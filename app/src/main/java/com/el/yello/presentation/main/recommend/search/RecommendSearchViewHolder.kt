package com.el.yello.presentation.main.recommend.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.databinding.ItemRecommendSearchBinding
import com.example.domain.entity.RecommendSearchModel.SearchFriendModel

class RecommendSearchViewHolder(
    val binding: ItemRecommendSearchBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: SearchFriendModel) {
        binding.tvRecommendItemName.text = item.name
        binding.tvRecommendItemId.text = item.yelloId
        binding.tvRecommendItemSchool.text = item.group
        item.profileImage.let { profileImage ->
            binding.ivRecommendItemThumbnail.load(profileImage) {
                transformations(CircleCropTransformation())
            }
        }

        if (item.isFriend) {
            binding.btnRecommendItemAdd.visibility = View.GONE
            binding.btnRecommendItemMyFriend.visibility = View.VISIBLE
        } else {
            binding.btnRecommendItemAdd.visibility = View.VISIBLE
            binding.btnRecommendItemMyFriend.visibility = View.GONE
        }
    }
}