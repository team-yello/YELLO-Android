package com.yello.presentation.main.myyello

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Yello
import com.example.domain.enum.GenderEnum
import com.yello.R
import com.yello.databinding.ItemMyYelloBinding

class MyYelloAdapter : RecyclerView.Adapter<MyYelloAdapter.MyYelloViewHolder>() {
    private val yelloList = mutableListOf<Yello>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyYelloViewHolder {
        val binding = ItemMyYelloBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyYelloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyYelloViewHolder, position: Int) {
        holder.onBind(yelloList[position])
    }

    override fun getItemCount(): Int = yelloList.size

    fun addItem(newItems: List<Yello>) {
        yelloList.addAll(newItems)
        notifyDataSetChanged()
    }

    class MyYelloViewHolder(
        private val binding: ItemMyYelloBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Yello) {
            binding.ivReadYelloPoint.isVisible = !item.isRead && !item.isHintUsed
            binding.tvTime.text = item.createdAt
            binding.clSendCheck.isVisible = item.isHintUsed
            binding.tvGender.isVisible = !item.isHintUsed
            if (item.gender == GenderEnum.M) {
                if (item.isHintUsed) {
                    binding.cardMyYello.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.semantic_gender_m_700
                        )
                    )
                }
                binding.ivYello.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_yello_blue
                    )
                )
                binding.tvGender.text = "남학생이 보냄"
            } else {
                if (item.isHintUsed) {
                    binding.cardMyYello.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.semantic_gender_f_700
                        )
                    )
                }
                binding.ivYello.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_yello_pink
                    )
                )
                binding.tvGender.text = "여학생이 보냄"
            }

            if (item.isHintUsed) {
                binding.tvNameHead.text = item.vote.nameHead
                binding.tvNameFoot.text = item.vote.nameFoot
                binding.tvKeywordHead.text = item.vote.keywordHead
                binding.tvKeyword.text = item.vote.keyword
                binding.tvKeywordFoot.text = item.vote.keywordFoot
                if (item.nameHint >= 0) {
                    binding.tvSendName.text = setChosungText(item.senderName, item.nameHint)
                }
                binding.clSendName.isVisible = item.nameHint != -1
            }
        }

        private fun setChosungText(name: String, number: Int): String {
            val firstChosung = name[number]
            val chosungUnicode = Character.UnicodeBlock.of(firstChosung.toInt())
            return if (chosungUnicode == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
                || chosungUnicode == Character.UnicodeBlock.HANGUL_JAMO
                || chosungUnicode == Character.UnicodeBlock.HANGUL_SYLLABLES
            ) {
                val chosungIndex = (firstChosung.toInt() - 0xAC00) / 28 / 21
                val chosung = Character.toChars(chosungIndex + 0x1100)[0]
                chosung.toString() // 출력: "ㄱ"
            } else ""
        }
    }
}

