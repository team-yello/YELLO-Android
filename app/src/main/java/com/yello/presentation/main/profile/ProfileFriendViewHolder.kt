package com.yello.presentation.main.profile

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.ProfileFriendModel
import com.example.ui.view.setOnSingleClickListener
import com.yello.databinding.ItemFriendsListBinding

class ProfileFriendViewHolder(val binding: ItemFriendsListBinding, private val itemClick: (ProfileFriendModel) -> (Unit)) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: ProfileFriendModel) {
        binding.tvProfileFriendItemName.text = item.name
        binding.tvProfileFriendItemSchool.text = item.school
        if (item.thumbnail != null) {
            binding.ivProfileFriendItemThumbnail.load(item.thumbnail) {
                transformations(CircleCropTransformation())
            }
        }

        binding.root.setOnSingleClickListener {
            itemClick(item)
        }
    }
}