package com.el.yello.presentation.main.profile.info

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.ItemProfileFriendsListBinding
import com.el.yello.presentation.main.profile.ProfileViewModel.Companion.BASIC_THUMBNAIL
import com.example.domain.entity.ProfileUserModel
import com.example.ui.view.setOnSingleClickListener

class ProfileFriendsListViewHolder(
    val binding: ItemProfileFriendsListBinding,
    private val itemClick: (ProfileUserModel, Int) -> (Unit),
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: ProfileUserModel, position: Int) {
        binding.tvProfileFriendItemName.text = item.name
        binding.tvProfileFriendItemSchool.text = item.group
        item.profileImageUrl.let { thumbnail ->
            if (thumbnail == BASIC_THUMBNAIL) {
                binding.ivProfileFriendItemThumbnail.load(R.drawable.img_yello_basic)
            } else {
                binding.ivProfileFriendItemThumbnail.load(thumbnail) {
                    transformations(CircleCropTransformation())
                }
            }
        }

        binding.root.setOnSingleClickListener {
            itemClick(item, position)
        }
    }
}
