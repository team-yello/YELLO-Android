package com.yello.presentation.main.profile

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.ProfileFriendModel
import com.yello.databinding.ItemFriendsListBinding

class ProfileFriendViewHolder(val binding: ItemFriendsListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: ProfileFriendModel) {
        binding.tvRecommendItemName.text = item.name
        binding.tvRecommendItemSchool.text = item.school
        if (item.thumbnail != null) {
            binding.ivRecommendItemThumbnail.load(item.thumbnail) {
                transformations(CircleCropTransformation())
            }
        }
    }
}