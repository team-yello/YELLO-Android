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
    private val itemClick: (RecommendFriend, Int) -> (Unit),

) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: RecommendFriend, position: Int) {
        binding.tvRecommendItemName.text = item.name
        binding.tvRecommendItemSchool.text = item.group
        item.profileImage.let { thumbnail ->
            if (thumbnail == BASIC_THUMBNAIL) {
                binding.ivRecommendItemThumbnail.load(R.drawable.img_yello_basic)
            } else {
                binding.ivRecommendItemThumbnail.load(thumbnail) {
                    transformations(CircleCropTransformation())
                }
            }
        }

        binding.btnRecommendItemAdd.setOnSingleClickListener {
            buttonClick(item, position, this)
        }

        binding.root.setOnSingleClickListener {
            itemClick(item, position)
        }
    }
}
