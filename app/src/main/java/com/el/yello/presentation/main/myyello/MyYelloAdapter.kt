package com.el.yello.presentation.main.myyello

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemMyYelloBinding
import com.el.yello.util.Utils
import com.example.domain.entity.Yello
import com.example.domain.enum.GenderEnum
import com.example.ui.view.setOnSingleClickListener

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
            Log.d("kangmi", item.gender.toString())
            binding.data = item
            binding.ivReadYelloPoint.isVisible = !item.isRead && !item.isHintUsed
            binding.tvTime.text = item.createdAt
            binding.clSendCheck.isVisible = item.isHintUsed
            binding.tvGender.isVisible = !item.isHintUsed
            binding.cardMyYello.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.grayscales_900
                )
            )
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
                    binding.tvSendName.text = Utils.setChosungText(item.senderName, item.nameHint)
                }
                binding.clSendName.isVisible = item.nameHint != -1
            }

            binding.root.setOnSingleClickListener {
                itemClick(item, position)
            }
        }
    }
}

