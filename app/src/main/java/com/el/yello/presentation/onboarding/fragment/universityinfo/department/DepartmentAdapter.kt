package com.el.yello.presentation.onboarding.fragment.universityinfo.department

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemDepartmentListBinding
import com.example.domain.entity.onboarding.Group
import com.example.ui.extension.setOnSingleClickListener
import com.example.ui.util.ItemDiffCallback

class DepartmentAdapter(
    private val storeDepartment: (String, Long) -> Unit,
) : ListAdapter<Group, DepartmentAdapter.DepartmentViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        return DepartmentViewHolder(
            ItemDepartmentListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeDepartment,
        )
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        holder.setDepartment(getItem(position))
    }

    fun addList(newItems: List<Group>) {
        val currentItems = currentList.toMutableList()
        currentItems.addAll(newItems)
        submitList(currentItems)
    }

    class DepartmentViewHolder(
        private val binding: ItemDepartmentListBinding,
        private val storeDepartment: (String, Long) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDepartment(department: Group) {
            with(binding) {
                data = department.name
                root.setOnSingleClickListener {
                    storeDepartment(department.name, department.groupId)
                }
                tvDepartmentName.setOnSingleClickListener {
                    storeDepartment(department.name, department.groupId)
                }
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<Group>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
