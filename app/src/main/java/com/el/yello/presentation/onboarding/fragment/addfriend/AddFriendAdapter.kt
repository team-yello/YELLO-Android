package com.el.yello.presentation.onboarding.fragment.addfriend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.el.yello.databinding.ItemAddFriendBinding
import com.example.domain.entity.onboarding.AddFriendListModel.FriendModel
import com.example.ui.extension.ItemDiffCallback

class AddFriendAdapter(private val itemClick: (FriendModel, Int) -> (Unit)) :
    ListAdapter<FriendModel, AddFriendViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        val binding: ItemAddFriendBinding =
            ItemAddFriendBinding.inflate(inflater, parent, false)
        return AddFriendViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: AddFriendViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    companion object {
        private val diffUtil = ItemDiffCallback<FriendModel>(
            onItemsTheSame = { old, new -> old.name == new.name },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
