package com.el.yello.presentation.main.profile.info

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.databinding.ItemFriendsListBinding
import com.example.domain.entity.ProfileUserModel
import com.example.ui.view.setOnSingleClickListener

class ProfileFriendViewHolder(
    val binding: ItemFriendsListBinding,
    private val itemClick: (ProfileUserModel, Int) -> (Unit)
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: ProfileUserModel, position: Int) {
        binding.tvProfileFriendItemName.text = item.name
        binding.tvProfileFriendItemSchool.text = item.group
        item.profileImageUrl.let { thumbnail ->
            binding.ivProfileFriendItemThumbnail.load(thumbnail) {
                transformations(CircleCropTransformation())
            }
        }

        binding.root.setOnSingleClickListener {
            itemClick(item, position)
        }
    }
}