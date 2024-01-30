package com.el.yello.presentation.main.profile.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemProfileFriendsListBinding
import com.el.yello.databinding.ItemProfileUserInfoBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.domain.entity.ProfileUserModel
import com.example.ui.view.ItemDiffCallback

class ProfileFriendAdapter(
    private val viewModel: ProfileViewModel,
    private val itemClick: (ProfileUserModel, Int) -> (Unit),
    private val shopClick: (Unit) -> (Unit),
    private val modClick: (Unit) -> (Unit),
) : ListAdapter<ProfileUserModel, RecyclerView.ViewHolder>(diffUtil) {

    private var itemList = mutableListOf<ProfileUserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }

        return when (viewType) {
            VIEW_TYPE_USER_INFO -> ProfileUserInfoViewHolder(
                ItemProfileUserInfoBinding.inflate(inflater, parent, false),
                shopClick,
                modClick
            )

            VIEW_TYPE_FRIENDS_LIST -> ProfileListInfoViewHolder(
                ItemProfileFriendsListBinding.inflate(inflater, parent, false),
                itemClick,
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
        when (holder) {
            is ProfileUserInfoViewHolder -> {
                holder.onBind(viewModel)
            }

            is ProfileListInfoViewHolder -> {
                val itemPosition = position - HEADER_COUNT
                holder.onBind(itemList[itemPosition], itemPosition)
            }
        }
        val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
        layoutParams.bottomMargin = if (position == itemList.size) 24 else 0
        holder.itemView.layoutParams = layoutParams
    }

    override fun getItemCount() = itemList.size + HEADER_COUNT

    override fun getItemViewType(position: Int) =
        when (position) {
            0 -> VIEW_TYPE_USER_INFO
            else -> VIEW_TYPE_FRIENDS_LIST
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
        if (this.itemList.isNotEmpty()) {
            this.itemList.removeAt(position)
            notifyItemRemoved(position + HEADER_COUNT)
            notifyItemRangeChanged(position + HEADER_COUNT, itemCount)
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<ProfileUserModel>(
            onItemsTheSame = { old, new -> old.userId == new.userId },
            onContentsTheSame = { old, new -> old == new },
        )

        private const val HEADER_COUNT = 1

        private const val VIEW_TYPE_USER_INFO = 0
        private const val VIEW_TYPE_FRIENDS_LIST = 1
    }
}
