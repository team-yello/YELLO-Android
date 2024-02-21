package com.el.yello.presentation.search

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemRecommendSearchBinding
import com.example.domain.entity.SearchListModel.SearchFriendModel
import com.example.ui.extension.setOnSingleClickListener

class SearchViewHolder(
    val binding: ItemRecommendSearchBinding,
    private val itemClick: (SearchFriendModel, Int, SearchViewHolder) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: SearchFriendModel, position: Int) {
        with(binding) {
            tvRecommendItemName.text = item.name
            tvRecommendItemId.text = String.format("@%s", item.yelloId)
            tvRecommendItemSchool.text = item.group

            ivRecommendItemThumbnail.setImageOrBasicThumbnail(item.profileImage)

            btnRecommendItemAdd.isVisible = !item.isFriend
            btnRecommendItemMyFriend.isVisible = item.isFriend

            btnRecommendItemAdd.setOnSingleClickListener {
                itemClick(item, position, this@SearchViewHolder)
            }
        }
    }
}