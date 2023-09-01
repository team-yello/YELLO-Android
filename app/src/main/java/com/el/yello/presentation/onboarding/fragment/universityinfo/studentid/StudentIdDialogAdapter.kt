package com.el.yello.presentation.onboarding.fragment.universityinfo.studentid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemStudentidListBinding
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

class StudentIdDialogAdapter(
    private val storeStudentId: (Int) -> Unit,
) : ListAdapter<Int, StudentIdDialogAdapter.StudentIdViewHolder>(diffUtil) {
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
                binding.tvItemStudentId.setBackgroundResource(R.drawable.shape_grayscales_800_fill_100_rect)
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
