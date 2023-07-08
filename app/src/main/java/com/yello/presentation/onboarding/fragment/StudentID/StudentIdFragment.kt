package com.yello.presentation.onboarding.fragment.StudentID

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentStudentidBinding
import com.yello.presentation.onboarding.fragment.StudentID.list.SearchDialogDepartmentFragment
import com.yello.presentation.onboarding.fragment.StudentID.list.SearchDialogIDFragment

class StudentIdFragment : BindingFragment<FragmentStudentidBinding>(R.layout.fragment_studentid) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchIDBtnClickListener()
        initSearchDeaprtmentBtnClickListener()
    }

    private fun initSearchIDBtnClickListener() {
        binding.etStudentidSearch.setOnSingleClickListener {
            SearchDialogIDFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun initSearchDeaprtmentBtnClickListener() {
        binding.etDepartmentSearch.setOnSingleClickListener {
            SearchDialogDepartmentFragment().show(parentFragmentManager, this.tag)
        }
    }
}
