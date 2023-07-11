package com.yello.presentation.onboarding.fragment.addfriend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.domain.entity.MyFriend
import com.example.ui.view.ItemDiffCallback
import com.yello.databinding.ItemAddfriendBinding

class AddFriendAdapter(requireContext: Context) : ListAdapter<MyFriend, AddFriendAdapter.AddFriendViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {
        return AddFriendViewHolder(
            ItemAddfriendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: AddFriendViewHolder, position: Int) {
        holder.setfriend(getItem(position))
    }

    class AddFriendViewHolder(private val binding: ItemAddfriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setfriend(friend: MyFriend) {
            binding.ivFriendProfile.load(friend.profile) {
                transformations(CircleCropTransformation())
            }
            binding.tvFriendName.text = friend.name
            binding.tvFriendDepartment.text = friend.department
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MyFriend>(
            onItemsTheSame = { old, new -> old.name == new.name },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
