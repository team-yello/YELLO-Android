package com.yello.presentation.onboarding.fragment.school.university

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener
import com.yello.databinding.ItemSchoolListBinding

class SchoolAdapter(
    private val storeSchool: (String) -> Unit,
) : ListAdapter<String, SchoolAdapter.SchoolViewHolder>(diffUtil) {
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
        fun setSchool(school: String) {
            binding.data = school
            binding.root.setOnSingleClickListener {
                storeSchool(school)
            }
            binding.tvSchoolName.setOnSingleClickListener {
                storeSchool(school)
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<String>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
