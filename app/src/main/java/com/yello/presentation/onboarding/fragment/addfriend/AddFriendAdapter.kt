package com.yello.presentation.onboarding.fragment.addfriend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.MyFriend
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ItemAddfriendBinding

class AddFriendAdapter(private val itemClick: (MyFriend, Int) -> (Unit)) :
    ListAdapter<MyFriend, AddFriendAdapter.AddFriendViewHolder>(diffUtil) {
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
        holder.setfriend(getItem(position), position)
    }

    class AddFriendViewHolder(
        private val binding: ItemAddfriendBinding,
        private val itemClick: (MyFriend, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setfriend(friend: MyFriend, position: Int) {
            binding.ivFriendProfile.load(friend.profile) {
                transformations(CircleCropTransformation())
            }
            binding.tvFriendName.text = friend.name
            binding.tvFriendDepartment.text = friend.department
            binding.ivFreindCheck.isSelected = friend.isSelcted
            binding.tvFriendName.setTextColor(ContextCompat.getColor(itemView.context, if (friend.isSelcted) R.color.white else R.color.grayscales_onbarding_light))
            binding.tvFriendDepartment.setTextColor(ContextCompat.getColor(itemView.context, if (friend.isSelcted) R.color.grayscales_500 else R.color.grayscales_onbarding_dark))

            binding.root.setOnSingleClickListener {
                itemClick(friend, position)
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MyFriend>(
            onItemsTheSame = { old, new -> old.name == new.name },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
