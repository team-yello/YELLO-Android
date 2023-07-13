package com.yello.presentation.onboarding.fragment.studentid.dialog.studentid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.MyStudentid
import com.example.ui.view.ItemDiffCallback
import com.yello.databinding.ItemStudentidListBinding

class StudentidDialogAdapter(requireContext: Context) :
    ListAdapter<MyStudentid, StudentidDialogAdapter.StudentidViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentidViewHolder {
        return StudentidViewHolder(
            ItemStudentidListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: StudentidViewHolder, position: Int) {
        holder.setStudentid(getItem(position))
    }

    class StudentidViewHolder(private val binding: ItemStudentidListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setStudentid(id: MyStudentid) {
            binding.data = id
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MyStudentid>(
            onItemsTheSame = { old, new -> old.studentid == new.studentid },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
