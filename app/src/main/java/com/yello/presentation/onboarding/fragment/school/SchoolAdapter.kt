package com.yello.presentation.onboarding.fragment.school

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.MySchool
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.databinding.ItemSchoolListBinding

class SchoolAdapter(
    requireContext: Context,
    private val storeSchool: (String) -> Unit,
) : ListAdapter<MySchool, SchoolAdapter.SchoolViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        return SchoolViewHolder(
            ItemSchoolListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeSchool,
        )
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {
        holder.setSchool(getItem(position))
    }

    class SchoolViewHolder(
        private val binding: ItemSchoolListBinding,
        private val storeSchool: (String) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setSchool(school: MySchool) {
            binding.data = school
            binding.root.setOnSingleClickListener {
                storeSchool(binding.tvSchoolName.text.toString())
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MySchool>(
            onItemsTheSame = { old, new -> old.groupNameList == new.groupNameList },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
