package com.yello.presentation.onboarding.fragment.addfriend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.onboarding.Friend
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ItemAddfriendBinding

class AddFriendAdapter(private val itemClick: (Friend, Int) -> (Unit)) :
    ListAdapter<Friend, AddFriendAdapter.AddFriendViewHolder>(diffUtil) {

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
        private val itemClick: (Friend, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(friend: Friend, position: Int) {
            binding.ivFriendProfile.load(friend.profileImage) {
                transformations(CircleCropTransformation())
            }
            binding.tvFriendName.text = friend.name
            binding.tvFriendDepartment.text = friend.groupName
            binding.ivFreindCheck.isSelected = friend.isSelected

            binding.tvFriendName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (friend.isSelected) R.color.white else R.color.grayscales_onbarding_light,
                ),
            )

            binding.tvFriendDepartment.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (friend.isSelected) R.color.grayscales_500 else R.color.grayscales_onbarding_dark,
                ),
            )

            binding.ivFreindCheck.setOnSingleClickListener {
                itemClick(friend, position)
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<Friend>(
            onItemsTheSame = { old, new -> old.name == new.name },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
