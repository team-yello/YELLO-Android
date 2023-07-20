package com.el.yello.presentation.main.profile.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.HeaderFriendsListBinding
import com.el.yello.databinding.ItemFriendsListBinding
import com.example.domain.entity.ProfileUserModel
import com.example.ui.view.ItemDiffCallback
import com.el.yello.presentation.main.profile.ProfileViewModel

class ProfileFriendAdapter(
    private val viewModel: ProfileViewModel,
    private val itemClick: (ProfileUserModel, Int) -> (Unit),
    private val buttonClick: (ProfileViewModel) -> (Unit)

) : ListAdapter<ProfileUserModel, RecyclerView.ViewHolder>(diffUtil) {

    private var itemList = mutableListOf<ProfileUserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // 멀티뷰타입 구현 - 헤더 & 아이템 리스트
        return when (viewType) {
            VIEW_TYPE_HEADER -> ProfileHeaderViewHolder(
                HeaderFriendsListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), buttonClick
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
            holder.onBind(viewModel)
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
        notifyItemRemoved(position + HEADER_COUNT)
        notifyItemRangeChanged(position + HEADER_COUNT, itemCount)
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