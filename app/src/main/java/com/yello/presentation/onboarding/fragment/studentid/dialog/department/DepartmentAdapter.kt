package com.yello.presentation.onboarding.fragment.studentid.dialog.department

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.MyDepartment
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.databinding.ItemDepartmentListBinding

class DepartmentAdapter(
    requireContext: Context,
    private val storeDepartment: (String) -> Unit,
) : ListAdapter<MyDepartment, DepartmentAdapter.DepartmentViewHolder>(diffUtil) {
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
        holder.setdepartment(getItem(position))
    }

    class DepartmentViewHolder(
        private val binding: ItemDepartmentListBinding,
        private val storeDepartment: (String) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setdepartment(department: MyDepartment) {
            binding.data = department
            binding.root.setOnSingleClickListener {
                storeDepartment(binding.tvDepartmentName.text.toString())
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MyDepartment>(
            onItemsTheSame = { old, new -> old.groupList == new.groupList },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
