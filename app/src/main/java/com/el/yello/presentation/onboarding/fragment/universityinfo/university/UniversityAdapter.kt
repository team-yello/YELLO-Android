package com.el.yello.presentation.onboarding.fragment.universityinfo.university

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemUniversityListBinding
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

class UniversityAdapter(
    private val storeUniversity: (String) -> Unit,
) : ListAdapter<String, UniversityAdapter.UniversityViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        return UniversityViewHolder(
            ItemUniversityListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeUniversity,
        )
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        holder.setUniversity(getItem(position))
    }

    fun addList(newItems: List<String>) {
        val currentItems = currentList.toMutableList()
        currentItems.addAll(newItems)
        submitList(currentItems)
    }

    class UniversityViewHolder(
        private val binding: ItemUniversityListBinding,
        private val storeUniversity: (String) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setUniversity(university: String) {
            with(binding) {
                universityList = university
                root.setOnSingleClickListener {
                    storeUniversity(university)
                }
                tvSchoolName.setOnSingleClickListener {
                    storeUniversity(university)
                }
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
