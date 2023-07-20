package com.yello.presentation.main.profile.info

import androidx.recyclerview.widget.RecyclerView
import com.example.ui.view.setOnSingleClickListener
import com.yello.databinding.HeaderFriendsListBinding
import com.yello.presentation.main.profile.ProfileViewModel

class ProfileHeaderViewHolder(
    val binding: HeaderFriendsListBinding,
    val buttonClick: (ProfileViewModel) -> (Unit)
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(viewModel: ProfileViewModel) {
        binding.vm = viewModel
        binding.executePendingBindings()

        binding.btnProfileAddGroup.setOnSingleClickListener {
            buttonClick(viewModel)
        }
    }
}