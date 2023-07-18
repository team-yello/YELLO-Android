package com.yello.presentation.onboarding.fragment.studentid.dialog.department

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.onboarding.GroupList
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.databinding.ItemDepartmentListBinding

class DepartmentAdapter(
    requireContext: Context,
    private val storeDepartment: (String, Long) -> Unit,
) : ListAdapter<GroupList, DepartmentAdapter.DepartmentViewHolder>(diffUtil) {
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
        fun setDepartment(department: GroupList) {
            binding.data = department.toString()
            binding.root.setOnSingleClickListener {
                storeDepartment("department.name", 0L) // TODO: department(String), groupId(Long) 넘겨주기
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<GroupList>(
            onItemsTheSame = { old, new -> old.group == new.group },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
