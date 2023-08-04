package com.el.yello.presentation.onboarding.fragment.addfriend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.ItemAddfriendBinding
import com.example.domain.entity.onboarding.AddFriendListModel.FriendModel
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

class AddFriendAdapter(private val itemClick: (FriendModel, Int) -> (Unit)) :
    ListAdapter<FriendModel, AddFriendAdapter.AddFriendViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {
        return AddFriendViewHolder(
            ItemAddfriendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            itemClick,
        )
    }

    override fun onBindViewHolder(holder: AddFriendViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    class AddFriendViewHolder(
        private val binding: ItemAddfriendBinding,
        private val itemClick: (FriendModel, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(friendModel: FriendModel, position: Int) {
            binding.ivFriendProfile.load(friendModel.profileImage) {
                transformations(CircleCropTransformation())
            }
            binding.tvFriendName.text = friendModel.name
            binding.tvFriendDepartment.text = friendModel.groupName
            binding.ivFreindCheck.isSelected = friendModel.isSelected

            binding.tvFriendName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (friendModel.isSelected) R.color.white else R.color.grayscales_onbarding_light,
                ),
            )

            binding.tvFriendDepartment.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (friendModel.isSelected) R.color.grayscales_500 else R.color.grayscales_onbarding_dark,
                ),
            )

            binding.ivFreindCheck.setOnSingleClickListener {
                itemClick(friendModel, position)
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<FriendModel>(
            onItemsTheSame = { old, new -> old.name == new.name },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
