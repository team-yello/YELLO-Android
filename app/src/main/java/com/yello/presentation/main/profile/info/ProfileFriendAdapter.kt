package com.yello.presentation.main.profile.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileUserModel
import com.example.ui.view.ItemDiffCallback
import com.yello.R
import com.yello.databinding.HeaderFriendsListBinding
import com.yello.databinding.ItemFriendsListBinding

class ProfileFriendAdapter(
    private val model: ProfileUserModel,
    private val itemClick: (ProfileFriendsListModel.ProfileFriendModel, Int) -> (Unit)
) : ListAdapter<ProfileFriendsListModel.ProfileFriendModel, RecyclerView.ViewHolder>(diffUtil) {

    private var itemList = mutableListOf<ProfileFriendsListModel.ProfileFriendModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // 멀티뷰타입 구현 - 헤더 & 아이템 리스트
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
        if (holder is ProfileHeaderViewHolder) {
            holder.onBind(model)
        }
        if (holder is ProfileFriendViewHolder) {
            val itemPosition = position - HEADER_COUNT
            holder.onBind(itemList[itemPosition], itemPosition)
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

    fun addItemList(newItems: List<ProfileFriendsListModel.ProfileFriendModel>) {
        this.itemList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setItemList(itemList: List<ProfileFriendsListModel.ProfileFriendModel>) {
        this.itemList = itemList.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position + HEADER_COUNT)
        notifyItemRangeChanged(position + HEADER_COUNT, itemCount)
    }

    companion object {
        private val diffUtil = ItemDiffCallback<ProfileFriendsListModel.ProfileFriendModel>(
            onItemsTheSame = { old, new -> old.userId == new.userId },
            onContentsTheSame = { old, new -> old == new },
        )

        private const val HEADER_COUNT = 1

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }
}