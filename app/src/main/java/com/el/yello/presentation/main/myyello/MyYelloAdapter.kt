package com.el.yello.presentation.main.myyello

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemMyYelloBinding
import com.example.domain.entity.Yello
import com.example.domain.enum.Gender
import com.example.ui.extension.setOnSingleClickListener

class MyYelloAdapter(private val itemClick: (Yello, Int) -> (Unit)) :
    RecyclerView.Adapter<MyYelloAdapter.MyYelloViewHolder>() {
    private val yelloList = mutableListOf<Yello>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyYelloViewHolder {
        val binding = ItemMyYelloBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyYelloViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: MyYelloViewHolder, position: Int) {
        holder.onBind(yelloList[position], position)
    }

    override fun getItemCount(): Int = yelloList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addItem(newItems: List<Yello>) {
        yelloList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearList() {
        yelloList.clear()
        notifyDataSetChanged()
    }

    fun currentList(): List<Yello> {
        return yelloList
    }

    fun changeItem(position: Int, newItem: Yello) {
        yelloList[position] = newItem
        notifyItemChanged(position)
    }

    class MyYelloViewHolder(
        private val binding: ItemMyYelloBinding,
        private val itemClick: (Yello, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Yello, position: Int) {
            binding.data = item
            binding.ivReadYelloPoint.isVisible = !item.isRead
            binding.tvTime.text = item.createdAt
            binding.clSendCheck.isVisible = (item.isHintUsed || item.nameHint != -1) && item.isRead
            binding.clMiddle.isGone = !item.isHintUsed && (item.nameHint == -2 || item.nameHint == -3) && item.isRead
            binding.clBottom.isGone = !item.isHintUsed && (item.nameHint == -2 || item.nameHint == -3) && item.isRead
            binding.tvGender.isVisible = (!item.isHintUsed && item.nameHint == -1) || !item.isRead
            binding.cardMyYello.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.grayscales_900
                )
            )
            binding.tvTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.grayscales_600))
            if (item.gender == Gender.M) {
                if ((item.isHintUsed || item.nameHint != -1) && item.isRead) {
                    binding.cardMyYello.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.semantic_gender_m_700
                        )
                    )
                    ContextCompat.getColor(itemView.context, R.color.semantic_gender_m_300).apply {
                        binding.tvSendName.setTextColor(this)
                        binding.tvSendNameEnd.setTextColor(this)
                    }
                    binding.tvTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.semantic_gender_m_500))
                }
                binding.ivYello.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_yello_blue
                    )
                )
                binding.tvGender.text = binding.root.context.getString(R.string.my_yello_send_by_boy)
            } else {
                if ((item.isHintUsed || item.nameHint != -1) && item.isRead) {
                    binding.cardMyYello.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.semantic_gender_f_700
                        )
                    )
                    ContextCompat.getColor(itemView.context, R.color.semantic_gender_f_300).apply {
                        binding.tvSendName.setTextColor(this)
                        binding.tvSendNameEnd.setTextColor(this)
                    }
                    binding.tvTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.semantic_gender_f_500))
                }
                binding.ivYello.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_yello_pink
                    )
                )
                binding.tvGender.text = binding.root.context.getString(R.string.my_yello_send_by_girl)
            }

            if (item.isHintUsed) {
                binding.tvNameHead.text = item.vote.nameHead
                binding.tvNameFoot.text = item.vote.nameFoot
                binding.tvKeywordHead.text = item.vote.keywordHead
                binding.tvKeyword.text = item.vote.keyword
                binding.tvKeywordFoot.text = item.vote.keywordFoot
                if (item.nameHint >= 0) {
                    binding.tvSendName.text = Utils.setChosungText(item.senderName, item.nameHint)
                }
                binding.clSendName.isVisible = item.nameHint != -1
            }
            if(item.nameHint == -2 || item.nameHint == -3) {
                binding.tvSendName.text = item.senderName
            }

            binding.root.setOnSingleClickListener {
                itemClick(item, position)
            }
        }
    }
}

