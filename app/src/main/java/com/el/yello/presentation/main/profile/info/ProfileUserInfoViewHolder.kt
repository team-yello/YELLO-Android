package com.el.yello.presentation.main.profile.info

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.databinding.ItemProfileUserInfoBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.view.setOnSingleClickListener

class ProfileUserInfoViewHolder(
    val binding: ItemProfileUserInfoBinding,
    val buttonClick: (ProfileViewModel) -> (Unit),
    val shopClick: (ProfileViewModel) -> (Unit)
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

        if (viewModel.isSubscribed) {
            binding.ivSubsLine.visibility = View.VISIBLE
            binding.ivSubsStar.visibility = View.VISIBLE
        } else {
            binding.ivSubsLine.visibility = View.GONE
            binding.ivSubsStar.visibility = View.GONE
        }

        binding.btnProfileAddGroup.setOnSingleClickListener {
            buttonClick(viewModel)
        }

        binding.btnProfileShop.setOnSingleClickListener {
            shopClick(viewModel)
        }
    }
}
