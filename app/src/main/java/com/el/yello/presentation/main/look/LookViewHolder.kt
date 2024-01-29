package com.el.yello.presentation.main.look

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemLookBinding
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.example.domain.entity.LookListModel.LookModel
import com.example.ui.context.colorOf
import com.example.ui.context.drawableOf

class LookViewHolder(
    val binding: ItemLookBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: LookModel) {
        with(binding) {
            tvLookName.text = item.receiverName
            tvLookTime.text = item.createdAt
            tvNameHead.text = item.vote.nameHead
            tvNameFoot.text = item.vote.nameFoot
            tvKeywordHead.text = item.vote.keywordHead
            tvKeywordFoot.text = item.vote.keywordFoot

            tvNameHead.isVisible = !item.vote.nameHead.isNullOrEmpty()
            tvKeywordHead.isVisible = !item.vote.keywordHead.isNullOrEmpty()

            ivLookThumbnail.setImageOrBasicThumbnail(item.receiverProfileImage)

            tvKeyword.apply {
                text = item.vote.keyword
                background =
                    if (item.isHintUsed) null else itemView.context.drawableOf(R.drawable.shape_grayscales800_fill_grayscales700_dashline_4_rect)
                setTextColor(
                    itemView.context.colorOf(
                        when {
                            item.isHintUsed && item.senderGender == MALE -> R.color.semantic_gender_m_300
                            item.isHintUsed && item.senderGender != MALE -> R.color.semantic_gender_f_300
                            else -> R.color.grayscales_800
                        },
                    ),
                )
            }

            tvLookSendGender.apply {
                text = if (item.senderGender == MALE) {
                    setTextColor(itemView.context.colorOf(R.color.semantic_gender_m_500))
                    FROM_MALE
                } else {
                    setTextColor(itemView.context.colorOf(R.color.semantic_gender_f_500))
                    FROM_FEMALE
                }
            }
        }
    }

    private companion object {
        const val MALE = "MALE"
        const val FROM_MALE = "남학생에게 받음"
        const val FROM_FEMALE = "여학생에게 받음"
    }
}
