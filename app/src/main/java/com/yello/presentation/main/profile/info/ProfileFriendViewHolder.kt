package com.yello.presentation.main.profile.info

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.ProfileUserModel
import com.example.ui.view.setOnSingleClickListener
import com.yello.databinding.ItemFriendsListBinding

class ProfileFriendViewHolder(
    val binding: ItemFriendsListBinding,
    private val itemClick: (ProfileUserModel) -> (Unit)
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: ProfileUserModel) {
        binding.tvProfileFriendItemName.text = item.name
        binding.tvProfileFriendItemSchool.text = item.group
        item.profileImageUrl.let { thumbnail ->
            binding.ivProfileFriendItemThumbnail.load(thumbnail) {
                transformations(CircleCropTransformation())
            }
        }

        binding.root.setOnSingleClickListener {
            itemClick(item)
        }
    }
}