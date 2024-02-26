package com.el.yello.presentation.onboarding.fragment.highschoolinfo.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemGroupListBinding
import com.example.ui.extension.setOnSingleClickListener
import com.example.ui.util.ItemDiffCallback

class GroupDialogAdapter(
    private val storeHighSchoolGroup: (String) -> Unit,
) : ListAdapter<String, GroupDialogAdapter.GroupViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(
            ItemGroupListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeHighSchoolGroup,
        )
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.setHighSchoolGroup(getItem(position))
    }

    class GroupViewHolder(
        private val binding: ItemGroupListBinding,
        private val storeHighSchoolGroup: (String) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setHighSchoolGroup(group: String) {
            binding.group = group
            binding.root.setOnSingleClickListener {
                storeHighSchoolGroup(group)
                binding.tvItemGroup.setBackgroundResource(R.drawable.shape_grayscales800_fill_100_rect)
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<String>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
