package com.yello.presentation.onboarding.fragment.school

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.MySchool
import com.example.ui.view.ItemDiffCallback
import com.yello.databinding.ItemSchoolListBinding
import timber.log.Timber

class SchoolAdpapter : ListAdapter<MySchool, SchoolAdpapter.SchoolViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        Timber.d("onCreateViewHolder")
        return SchoolViewHolder(
            ItemSchoolListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {
        Timber.d("onBindViewHolder($position)")
        holder.setlist(getItem(position))
    }

    class SchoolViewHolder(private val binding: ItemSchoolListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setlist(school: MySchool) {
            Timber.d("set list : $school")
            binding.data = school
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MySchool>(
            onItemsTheSame = { old, new -> old.schoolname == new.schoolname },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
