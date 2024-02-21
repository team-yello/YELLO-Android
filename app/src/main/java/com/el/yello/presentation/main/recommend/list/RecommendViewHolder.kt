package com.el.yello.presentation.main.recommend.list

import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemRecommendListBinding
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.example.domain.entity.RecommendListModel.RecommendFriend
import com.example.ui.extension.setOnSingleClickListener

class RecommendViewHolder(
    val binding: ItemRecommendListBinding,
    private val buttonClick: (RecommendFriend, Int, RecommendViewHolder) -> Unit,
    private val itemClick: (RecommendFriend, Int, RecommendViewHolder) -> (Unit),
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: RecommendFriend, position: Int) {
        with(binding) {
            tvRecommendItemName.text = item.name
            tvRecommendItemSchool.text = item.group

            ivRecommendItemThumbnail.setImageOrBasicThumbnail(item.profileImage.orEmpty())

            btnRecommendItemAdd.setOnSingleClickListener {
                buttonClick(item, position, this@RecommendViewHolder)
            }

            root.setOnSingleClickListener {
                itemClick(item, position, this@RecommendViewHolder)
            }
        }
    }
}
