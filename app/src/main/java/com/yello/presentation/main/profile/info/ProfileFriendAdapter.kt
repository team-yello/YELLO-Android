package com.yello.presentation.main.profile.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.ProfileUserModel
import com.example.ui.view.ItemDiffCallback
import com.yello.R
import com.yello.databinding.HeaderFriendsListBinding
import com.yello.databinding.ItemFriendsListBinding

class ProfileFriendAdapter(
    private val model: ProfileUserModel,
    private val itemClick: (ProfileUserModel, Int) -> (Unit)
) :
    ListAdapter<ProfileUserModel, RecyclerView.ViewHolder>(diffUtil) {

    private var itemList = mutableListOf<ProfileUserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_HEADER -> ProfileHeaderViewHolder(
                HeaderFriendsListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            VIEW_TYPE_ITEM -> ProfileFriendViewHolder(
                ItemFriendsListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), itemClick
            )

            else -> throw ClassCastException(
                parent.context.getString(
                    R.string.view_type_error_msg,
                    viewType,
                ),
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProfileFriendViewHolder) {
            val itemPosition = position - HEADER_COUNT
            holder.onBind(itemList[itemPosition], itemPosition)
        }
        if (holder is ProfileHeaderViewHolder) {
            holder.onBind(model)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size + HEADER_COUNT
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_ITEM
        }
    }

    fun addItemList(newItems: List<ProfileUserModel>) {
        this.itemList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setItemList(itemList: List<ProfileUserModel>) {
        this.itemList = itemList.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    companion object {
        private val diffUtil = ItemDiffCallback<ProfileUserModel>(
            onItemsTheSame = { old, new -> old.userId == new.userId },
            onContentsTheSame = { old, new -> old == new },
        )

        private const val HEADER_COUNT = 1

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }
}