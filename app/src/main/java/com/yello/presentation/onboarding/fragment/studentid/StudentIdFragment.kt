package com.yello.presentation.onboarding.fragment.studentid

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentStudentidBinding
import com.yello.presentation.onboarding.fragment.studentid.dialog.department.SearchDialogDepartmentFragment
import com.yello.presentation.onboarding.fragment.studentid.dialog.studentid.StudentidDialogFragment

class StudentIdFragment : BindingFragment<FragmentStudentidBinding>(R.layout.fragment_studentid) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchDepartmentBtnClickListener()
        initSearchStudentidBtnClickListener()
    }
    private fun initSearchDepartmentBtnClickListener() {
        binding.tvDepartmentSearch.setOnSingleClickListener {
            SearchDialogDepartmentFragment().show(parentFragmentManager, this.tag)
        }
    }
    private fun initSearchStudentidBtnClickListener() {
        binding.tvStudentidSearch.setOnSingleClickListener {
            StudentidDialogFragment().show(parentFragmentManager, this.tag)
        }
    }
}
