package com.el.yello.presentation.main.recommend.list

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R
import com.el.yello.databinding.ItemRecommendListBinding
import com.el.yello.presentation.main.profile.ProfileViewModel.Companion.BASIC_THUMBNAIL
import com.example.domain.entity.RecommendListModel.RecommendFriend
import com.example.ui.view.setOnSingleClickListener

class RecommendViewHolder(
    val binding: ItemRecommendListBinding,
    private val buttonClick: (RecommendFriend, Int, RecommendViewHolder) -> Unit,
    private val itemClick: (RecommendFriend, Int, RecommendViewHolder) -> (Unit),
    ) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: RecommendFriend, position: Int) {
        with(binding) {
            tvRecommendItemName.text = item.name
            tvRecommendItemSchool.text = item.group

            ivRecommendItemThumbnail.apply {
                val thumbnail = item.profileImage
                load(if (thumbnail == BASIC_THUMBNAIL) R.drawable.img_yello_basic else thumbnail) {
                    transformations(CircleCropTransformation())
                }
            }

            btnRecommendItemAdd.setOnSingleClickListener {
                buttonClick(item, position, this@RecommendViewHolder)
            }

            root.setOnSingleClickListener {
                itemClick(item, position, this@RecommendViewHolder)
            }
        }
    }
}
