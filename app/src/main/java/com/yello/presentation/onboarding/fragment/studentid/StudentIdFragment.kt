package com.yello.presentation.onboarding.fragment.studentid

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentStudentidBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.yello.presentation.onboarding.fragment.studentid.dialog.department.SearchDialogDepartmentFragment
import com.yello.presentation.onboarding.fragment.studentid.dialog.studentid.StudentidDialogFragment

class StudentIdFragment : BindingFragment<FragmentStudentidBinding>(R.layout.fragment_studentid) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initSearchDepartmentBtnClickListener()
        initSearchStudentidBtnClickListener()
        setConfirmBtnClickListener()
        setBackBtnClickListener()
        setupDepartment()
        setupStudentid()
        binding.tvDepartmentSearch.doAfterTextChanged {
            it.toString()
        }
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

    private fun setupStudentid() {
        viewModel._studentid.observe(viewLifecycleOwner) { studentid ->
            binding.tvStudentidSearch.text = studentid
        }
    }
}
