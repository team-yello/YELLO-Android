package com.yello.presentation.onboarding.fragment.studentid.dialog.studentid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.MyStudentid
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ItemStudentidListBinding

class StudentidDialogAdapter(
    requireContext: Context,
    private val storeStudentId: (String) -> Unit,
) : ListAdapter<MyStudentid, StudentidDialogAdapter.StudentIdViewHolder>(diffUtil) {
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
        private val storeStudentId: (String) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setStudentId(id: MyStudentid) {
            binding.data = id
            binding.root.setOnSingleClickListener {
                storeStudentId(binding.tvItemStudentId.text.toString())
                binding.tvItemStudentId.setBackgroundResource(R.drawable.shape_grayscales_800_fill_8_rect)
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MyStudentid>(
            onItemsTheSame = { old, new -> old.studentid == new.studentid },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
