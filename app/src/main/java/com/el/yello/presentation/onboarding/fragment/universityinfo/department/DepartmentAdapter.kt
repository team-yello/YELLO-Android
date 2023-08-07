package com.el.yello.presentation.onboarding.fragment.universityinfo.department

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemDepartmentListBinding
import com.example.domain.entity.onboarding.Group
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

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

    class DepartmentViewHolder(
        private val binding: ItemDepartmentListBinding,
        private val storeDepartment: (String, Long) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDepartment(department: Group) {
            binding.data = department.name
            binding.root.setOnSingleClickListener {
                storeDepartment(department.name, department.groupId)
            }
            binding.tvDepartmentName.setOnSingleClickListener {
                storeDepartment(department.name, department.groupId)
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
