package com.el.yello.presentation.main.profile.info

import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemProfileFriendsListBinding
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.example.domain.entity.ProfileUserModel
import com.example.ui.extension.setOnSingleClickListener

class ProfileListInfoViewHolder(
    val binding: ItemProfileFriendsListBinding,
    private val itemClick: (ProfileUserModel, Int) -> (Unit),
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: ProfileUserModel, position: Int) {
        with(binding) {
            tvProfileFriendItemName.text = item.name
            tvProfileFriendItemSchool.text = item.group

            ivProfileFriendItemThumbnail.setImageOrBasicThumbnail(item.profileImageUrl)

            root.setOnSingleClickListener {
                itemClick(item, position)
            }
        }
    }
}
