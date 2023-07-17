package com.yello.presentation.main.recommend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.ProfileFriendModel
import com.example.domain.entity.RecommendModel
import com.example.ui.intent.dpToPx
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ItemRecommendListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RecommendAdapter(private val itemClick: (RecommendModel) -> (Unit)) :
    RecyclerView.Adapter<RecommendViewHolder>() {

    private var itemList = mutableListOf<RecommendModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val inflater by lazy { LayoutInflater.from(parent.context) }
        val binding: ItemRecommendListBinding =
            ItemRecommendListBinding.inflate(inflater, parent, false)
        return RecommendViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.onBind(itemList[position])

        changeToTextButton(holder)
        initItemAddButtonListener(holder, position)
    }


    override fun getItemCount(): Int = itemList.size

    fun setItemList(itemList: List<RecommendModel>) {
        this.itemList = itemList.toMutableList()
        notifyDataSetChanged()
    }


    private fun changeToCheckIcon(holder: RecommendViewHolder) {
        holder.binding.btnRecommendItemAdd.apply {
            text = null
            setIconResource(R.drawable.ic_check)
            setIconTintResource(R.color.black)
            iconPadding = dpToPx(holder.binding.root.context, -2)
            setPadding(dpToPx(holder.binding.root.context, 10))
        }
    }

    private fun changeToTextButton(holder: RecommendViewHolder) {
        holder.binding.btnRecommendItemAdd.apply {
            text = "친구추가"
            icon = null
            setPadding(
                dpToPx(holder.binding.root.context, 13),
                0,
                dpToPx(holder.binding.root.context, 13),
                0
            )
        }
    }

    private fun initItemAddButtonListener(holder: RecommendViewHolder, position: Int) {
        holder.binding.btnRecommendItemAdd.setOnSingleClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                changeToCheckIcon(holder)
                delay(300)
                removeItem(position)
            }
        }
    }

    private fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }
}