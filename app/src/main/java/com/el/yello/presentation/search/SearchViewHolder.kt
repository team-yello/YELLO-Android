package com.el.yello.presentation.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.ItemRecommendSearchBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.example.domain.entity.SearchListModel.SearchFriendModel
import com.example.ui.view.setOnSingleClickListener

class SearchViewHolder(
    val binding: ItemRecommendSearchBinding,
    private val itemClick: (SearchFriendModel, Int, SearchViewHolder) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: SearchFriendModel, position: Int) {
        binding.tvRecommendItemName.text = item.name
        binding.tvRecommendItemId.text = String.format("@%s", item.yelloId)
        binding.tvRecommendItemSchool.text = item.group

        binding.ivRecommendItemThumbnail.setImageOrBasicThumbnail(item.profileImage)

        if (item.isFriend) {
            binding.btnRecommendItemAdd.visibility = View.GONE
            binding.btnRecommendItemMyFriend.visibility = View.VISIBLE
        } else {
            binding.btnRecommendItemAdd.visibility = View.VISIBLE
            binding.btnRecommendItemMyFriend.visibility = View.GONE
        }

        binding.btnRecommendItemAdd.setOnSingleClickListener {
            itemClick(item, position, this)
        }
    }
}