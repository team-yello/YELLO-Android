package com.yello.presentation.main.profile.info

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.ProfileUserModel
import com.yello.databinding.HeaderFriendsListBinding
import com.yello.presentation.main.profile.ProfileViewModel

class ProfileHeaderViewHolder(
    val binding: HeaderFriendsListBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(viewModel: ProfileViewModel) {
        binding.vm = viewModel
        binding.executePendingBindings()
    }
}