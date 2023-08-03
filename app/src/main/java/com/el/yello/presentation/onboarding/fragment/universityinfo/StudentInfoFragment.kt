package com.el.yello.presentation.onboarding.fragment.universityinfo

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentSchoolBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.universityinfo.department.SearchDialogDepartmentFragment
import com.el.yello.presentation.onboarding.fragment.universityinfo.school.SearchDialogSchoolFragment
import com.el.yello.presentation.onboarding.fragment.universityinfo.studentid.StudentIdDialogFragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import java.util.Timer
import kotlin.concurrent.timer

class StudentInfoFragment : BindingFragment<FragmentSchoolBinding>(R.layout.fragment_school) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.progressbar.progress = 20
        initSearchInfoBtnClickListener()
        setConfirmBtnClickListener()
        setupSchool()
        setupDepartment()
        setupStudentId()
        initSearchInfoBtnClickListener()

        binding.btn.setOnClickListener { ProgressBarTimerFun() }
    }
    fun ProgressBarTimerFun() {
        deltaTime = 20 // 기존의 20%를 채움
        binding.progressbar.progress = 20
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 10, initialDelay = 1000) {
            if (deltaTime > 40) cancel()
            binding.progressbar.setProgress(++deltaTime)
            println(binding.progressbar.progress)
        }
    }

    private fun initSearchInfoBtnClickListener() {
        binding.tvSchoolSearch.setOnSingleClickListener {
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
        viewModel._school.observe(viewLifecycleOwner) { school ->
            binding.tvSchoolSearch.text = school
        }
    }
    private fun setupDepartment() {
        viewModel._department.observe(viewLifecycleOwner) { department ->
            binding.tvDepartmentSearch.text = department
        }
        binding.tvDepartmentSearch.doAfterTextChanged {
            it.toString()
        }
    }

    private fun setupStudentId() {
        viewModel._studentId.observe(viewLifecycleOwner) { studentId ->
            binding.tvStudentidSearch.text = getString(R.string.onboarding_student_id, studentId)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnStudentinfoNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
        binding.btnStudentinfoBackBtn.setOnSingleClickListener {
            viewModel.navigateToBackPage()
        }
    }
}
