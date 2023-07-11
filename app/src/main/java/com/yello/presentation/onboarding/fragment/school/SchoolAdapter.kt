package com.yello.presentation.onboarding.fragment.school

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.MySchool
import com.example.ui.view.ItemDiffCallback
import com.yello.databinding.ItemSchoolListBinding

class SchoolAdapter(requireContext: Context) : ListAdapter<MySchool, SchoolAdapter.SchoolViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        return SchoolViewHolder(
            ItemSchoolListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {
        holder.setschool(getItem(position))
    }

    class SchoolViewHolder(private val binding: ItemSchoolListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setschool(school: MySchool) {
            binding.tvSchoolName.text = school.schoolname
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MySchool>(
            onItemsTheSame = { old, new -> old.schoolname == new.schoolname },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
