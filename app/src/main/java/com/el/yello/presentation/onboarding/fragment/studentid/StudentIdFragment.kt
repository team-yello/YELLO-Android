package com.el.yello.presentation.onboarding.fragment.studentid

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentStudentidBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.studentid.dialog.department.SearchDialogDepartmentFragment
import com.el.yello.presentation.onboarding.fragment.studentid.dialog.studentid.StudentidDialogFragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class StudentIdFragment : BindingFragment<FragmentStudentidBinding>(R.layout.fragment_studentid) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initSearchDepartmentBtnClickListener()
        initSearchStudentIdBtnClickListener()
        setConfirmBtnClickListener()
        setBackBtnClickListener()
        setupDepartment()
        setupStudentId()
        binding.tvDepartmentSearch.doAfterTextChanged {
            it.toString()
        }
    }

    private fun initSearchDepartmentBtnClickListener() {
        binding.tvDepartmentSearch.setOnSingleClickListener {
            SearchDialogDepartmentFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun initSearchStudentIdBtnClickListener() {
        binding.tvStudentidSearch.setOnSingleClickListener {
            StudentidDialogFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnStdentidNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
    }

    private fun setBackBtnClickListener() {
        binding.btnStudentidBackBtn.setOnSingleClickListener {
            viewModel.navigateToBackPage()
        }
    }

    private fun setupDepartment() {
        viewModel._department.observe(viewLifecycleOwner) { department ->
            binding.tvDepartmentSearch.text = department
        }
    }

    private fun setupStudentId() {
        viewModel._studentId.observe(viewLifecycleOwner) { studentId ->
            binding.tvStudentidSearch.text = getString(R.string.onboarding_student_id, studentId)
        }
    }
}
