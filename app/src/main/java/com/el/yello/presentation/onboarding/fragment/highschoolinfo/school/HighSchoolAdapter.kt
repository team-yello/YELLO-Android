package com.el.yello.presentation.onboarding.fragment.highschoolinfo.school

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemHighschoolListBinding
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

class HighSchoolAdapter(
    private val storeHighSchool: (String) -> Unit,
) : ListAdapter<String, HighSchoolAdapter.HighSchoolViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighSchoolViewHolder {
        return HighSchoolViewHolder(
            ItemHighschoolListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeHighSchool,
        )
    }

    override fun onBindViewHolder(holder: HighSchoolViewHolder, position: Int) {
        holder.setHighSchool(getItem(position))
    }

    class HighSchoolViewHolder(
        private val binding: ItemHighschoolListBinding,
        private val storeHighSchool: (String) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setHighSchool(highschool: String) {
            binding.data = highschool
            binding.root.setOnSingleClickListener {
                storeHighSchool(highschool)
            }
            binding.tvHighschoolName.setOnSingleClickListener {
                storeHighSchool(highschool)
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
