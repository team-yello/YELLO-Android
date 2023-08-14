package com.el.yello.presentation.main.look

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemLookBinding
import com.example.domain.entity.LookListModel.LookModel

class LookViewHolder(
    val binding: ItemLookBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: LookModel) {
        binding.tvLookName.text = item.receiverName
        binding.tvLookTime.text = item.createdAt
        binding.tvNameHead.text = item.vote.nameHead
        binding.tvNameFoot.text = item.vote.nameFoot
        binding.tvKeywordHead.text = item.vote.keywordHead
        binding.tvKeyword.text = item.vote.keyword
        binding.tvKeywordFoot.text = item.vote.keywordFoot

        if (item.vote.nameHead == null) {
            binding.tvNameHead.visibility = View.GONE
        }

        if (item.vote.keywordHead == null) {
            binding.tvKeywordHead.visibility = View.GONE
        }

        if (item.senderGender == MALE) {
            binding.tvLookGender.text = MALE
            binding.ivLookGender.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context, R.drawable.ic_yello_blue
                )
            )
        } else {
            binding.tvLookGender.text = FEMALE
            binding.ivLookGender.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context, R.drawable.ic_yello_pink
                )
            )
        }

        if (item.isHintUsed) {
            binding.tvKeyword.background = null
            if (item.senderGender == MALE) {
                binding.tvKeyword.setTextColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.semantic_gender_m_300
                    )
                )
            } else {
                binding.tvKeyword.setTextColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.semantic_gender_f_300
                    )
                )
            }
        } else {
            binding.tvKeyword.background = ContextCompat.getDrawable(
                itemView.context, R.drawable.shape_grayscales800_fill_grayscales700_dashline_4_rect
            )
        }
    }

    private companion object {
        const val MALE = "남학생"
        const val FEMALE = "여학생"
    }
}