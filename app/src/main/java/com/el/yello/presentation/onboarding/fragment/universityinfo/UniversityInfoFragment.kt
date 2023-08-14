package com.el.yello.presentation.onboarding.fragment.universityinfo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentUniversityBinding
import com.el.yello.presentation.auth.SocialSyncActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.universityinfo.department.SearchDialogDepartmentFragment
import com.el.yello.presentation.onboarding.fragment.universityinfo.school.SearchDialogSchoolFragment
import com.el.yello.presentation.onboarding.fragment.universityinfo.studentid.StudentIdDialogFragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import java.util.Timer
import kotlin.concurrent.timer

class UniversityInfoFragment :
    BindingFragment<FragmentUniversityBinding>(R.layout.fragment_university) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initSearchInfoBtnClickListener()
        setConfirmBtnClickListener()
        setupSchool()
        setupDepartment()
        setupStudentId()
        initSearchInfoBtnClickListener()
    }

    private fun initSearchInfoBtnClickListener() {
        binding.tvUniversitySearch.setOnSingleClickListener {
            SearchDialogSchoolFragment().show(parentFragmentManager, this.tag)
        }
        binding.tvDepartmentSearch.setOnSingleClickListener {
            SearchDialogDepartmentFragment().show(parentFragmentManager, this.tag)
        }
        binding.tvStudentidSearch.setOnSingleClickListener {
            StudentIdDialogFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun setupSchool() {
        viewModel.schoolText.observe(viewLifecycleOwner) { school ->
            binding.tvUniversitySearch.text = school
        }
    }
    private fun setupDepartment() {
        viewModel.departmentText.observe(viewLifecycleOwner) { department ->
            binding.tvDepartmentSearch.text = department
        }
        binding.tvDepartmentSearch.doAfterTextChanged {
            it.toString()
        }
    }

    private fun setupStudentId() {
        viewModel.studentIdText.observe(viewLifecycleOwner) { studentId ->
            binding.tvStudentidSearch.text = getString(R.string.onboarding_student_id, studentId)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnUniversityInfoNext.setOnSingleClickListener {
            findNavController().navigate(R.id.action_universityInfoFragment_to_genderFragment)
        }
    }
}
