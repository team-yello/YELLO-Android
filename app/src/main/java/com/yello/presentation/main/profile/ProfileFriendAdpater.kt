package com.yello.presentation.main.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.ProfileFriendModel
import com.yello.databinding.ItemFriendsListBinding

class ProfileFriendAdapter(context: Context) : RecyclerView.Adapter<ProfileFriendViewHolder>() {

    private val inflater by lazy { LayoutInflater.from(context) }
    private var itemList = mutableListOf<ProfileFriendModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFriendViewHolder {
        val binding: ItemFriendsListBinding =
            ItemFriendsListBinding.inflate(inflater, parent, false)
        return ProfileFriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileFriendViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    fun setItemList(itemList: List<ProfileFriendModel>) {
        this.itemList = itemList.toMutableList()
        notifyDataSetChanged()
    }
}