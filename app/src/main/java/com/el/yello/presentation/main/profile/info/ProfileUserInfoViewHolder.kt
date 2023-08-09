package com.el.yello.presentation.main.profile.info

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.databinding.ItemProfileUserInfoBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.view.setOnSingleClickListener

class ProfileUserInfoViewHolder(
    val binding: ItemProfileUserInfoBinding,
    val buttonClick: (ProfileViewModel) -> (Unit),
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(viewModel: ProfileViewModel) {
        binding.vm = viewModel
        binding.executePendingBindings()
        if (viewModel.myThumbnail.value != "") {
            binding.ivProfileInfoThumbnail.load(viewModel.myThumbnail.value) {
                transformations(CircleCropTransformation())
            }
        }

        binding.btnProfileAddGroup.setOnSingleClickListener {
            buttonClick(viewModel)
        }
    }
}
