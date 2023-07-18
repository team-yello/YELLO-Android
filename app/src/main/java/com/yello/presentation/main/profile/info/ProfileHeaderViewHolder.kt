package com.yello.presentation.main.profile.info

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.ProfileUserModel
import com.yello.databinding.HeaderFriendsListBinding

class ProfileHeaderViewHolder(
    val binding: HeaderFriendsListBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: ProfileUserModel) {
        binding.model = item
    }
}