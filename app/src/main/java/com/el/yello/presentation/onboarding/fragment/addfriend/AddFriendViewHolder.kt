package com.el.yello.presentation.onboarding.fragment.addfriend

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.ItemAddFriendBinding
import com.example.domain.entity.onboarding.AddFriendListModel
import com.example.ui.view.setOnSingleClickListener

class AddFriendViewHolder(
    private val binding: ItemAddFriendBinding,
    private val itemClick: (AddFriendListModel.FriendModel, Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(friendModel: AddFriendListModel.FriendModel, position: Int) {
        with(binding) {
            ivFriendProfile.load(friendModel.profileImage) {
                transformations(CircleCropTransformation())
            }
            tvFriendName.text = friendModel.name
            tvFriendDepartment.text = friendModel.groupName
            btnFriendCheck.isSelected = friendModel.isSelected

            tvFriendName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (friendModel.isSelected) R.color.white else R.color.grayscales_onbarding_light,
                ),
            )

            tvFriendDepartment.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (friendModel.isSelected) R.color.grayscales_500 else R.color.grayscales_onbarding_dark,
                ),
            )

            btnFriendCheck.setOnSingleClickListener {
                itemClick(friendModel, position)
            }
        }
    }
}
