package com.el.yello.presentation.main.look

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.el.yello.R
import com.el.yello.databinding.ItemLookBinding
import com.example.domain.entity.LookListModel.LookModel

class LookViewHolder(
    val binding: ItemLookBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: LookModel) {
        with(binding) {
            tvLookName.text = item.receiverName
            tvLookTime.text = item.createdAt
            tvNameHead.text = item.vote.nameHead
            tvNameFoot.text = item.vote.nameFoot
            tvKeywordHead.text = item.vote.keywordHead
            tvKeyword.text = item.vote.keyword
            tvKeywordFoot.text = item.vote.keywordFoot

            tvNameHead.visibility =
                if (item.vote.nameHead.isNullOrEmpty()) View.GONE else View.VISIBLE
            tvKeywordHead.visibility =
                if (item.vote.keywordHead.isNullOrEmpty()) View.GONE else View.VISIBLE

            ivLookThumbnail.load(R.drawable.img_yello_basic)
        }

        if (item.senderGender == MALE) {
            setItemViewTextColor(binding.tvLookSendGender, R.color.semantic_gender_m_500)
            binding.tvLookSendGender.text = "남학생에게 받음"
        } else {
            setItemViewTextColor(binding.tvLookSendGender, R.color.semantic_gender_f_500)
            binding.tvLookSendGender.text = "여학생에게 받음"
        }

        if (item.isHintUsed) {
            binding.tvKeyword.background = null
            if (item.senderGender == MALE) {
                setItemViewTextColor(binding.tvKeyword, R.color.semantic_gender_m_300)
            } else {
                setItemViewTextColor(binding.tvKeyword, R.color.semantic_gender_f_300)
            }
        } else {
            binding.tvKeyword.background = ContextCompat.getDrawable(
                itemView.context, R.drawable.shape_grayscales800_fill_grayscales700_dashline_4_rect
            )
            setItemViewTextColor(binding.tvKeyword, R.color.grayscales_800)
        }
    }

    private fun setItemViewTextColor(textView: TextView, resourceId: Int) {
        textView.setTextColor(
            ContextCompat.getColor(
                itemView.context, resourceId
            )
        )
    }

    private companion object {
        const val MALE = "MALE"
    }
}