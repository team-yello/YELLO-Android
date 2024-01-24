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
        with(binding) {
            tvProfileFriendItemName.text = item.name
            tvProfileFriendItemSchool.text = item.group

            ivProfileFriendItemThumbnail.apply {
                val thumbnail = item.profileImageUrl
                load(if (thumbnail == BASIC_THUMBNAIL) R.drawable.img_yello_basic else thumbnail) {
                    transformations(CircleCropTransformation())
                }
            }

            root.setOnSingleClickListener {
                itemClick(item, position)
            }
        }
    }
}
