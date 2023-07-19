package com.yello.presentation.onboarding.fragment.studentid.dialog.studentid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ItemStudentidListBinding

class StudentidDialogAdapter(
    private val storeStudentId: (Int) -> Unit,
) : ListAdapter<Int, StudentidDialogAdapter.StudentIdViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentIdViewHolder {
        return StudentIdViewHolder(
            ItemStudentidListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeStudentId,
        )
    }

    override fun onBindViewHolder(holder: StudentIdViewHolder, position: Int) {
        holder.setStudentId(getItem(position))
    }

    class StudentIdViewHolder(
        private val binding: ItemStudentidListBinding,
        private val storeStudentId: (Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setStudentId(id: Int) {
            binding.studentId = id
            binding.root.setOnSingleClickListener {
                storeStudentId(id)
                binding.tvItemStudentId.setBackgroundResource(R.drawable.shape_grayscales800_fill_8_rect)
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<Int>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
