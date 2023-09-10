package com.el.yello.presentation.onboarding.fragment.universityinfo.university

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemUniversityListBinding
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

class UniversityAdapter(
    private val storeSchool: (String) -> Unit,
) : ListAdapter<String, UniversityAdapter.SchoolViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        return SchoolViewHolder(
            ItemUniversityListBinding.inflate(
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
        private val binding: ItemUniversityListBinding,
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
