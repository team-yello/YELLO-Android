package com.el.yello.presentation.onboarding.fragment.universityinfo

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentUniversityBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.universityinfo.department.SearchDialogDepartmentFragment
import com.el.yello.presentation.onboarding.fragment.universityinfo.studentid.StudentIdDialogFragment
import com.el.yello.presentation.onboarding.fragment.universityinfo.university.SearchDialogUniversityFragment
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class UniversityInfoFragment :
    BindingFragment<FragmentUniversityBinding>(R.layout.fragment_university) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initSearchInfoBtnClickListener()
        setConfirmBtnClickListener()
        setupUniversity()
        setupDepartment()
        setupStudentId()
    }
    override fun onResume() {
        super.onResume()
        (activity as? OnBoardingActivity)?.showBackBtn()
    }

    private fun initSearchInfoBtnClickListener() {
        binding.tvUniversitySearch.setOnSingleClickListener {
            SearchDialogUniversityFragment().show(parentFragmentManager, this.tag)
        }
        binding.tvDepartmentSearch.setOnSingleClickListener {
            if (binding.tvUniversitySearch.text.isNotBlank()) {
                SearchDialogDepartmentFragment().show(parentFragmentManager, this.tag)
            } else {
                yelloSnackbar(binding.root.rootView, "학교를 선택해야 학과를 선택할 수 있어요!")
            }
        }
        binding.tvStudentidSearch.setOnSingleClickListener {
            StudentIdDialogFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun setupUniversity() {
        viewModel.universityText.observe(viewLifecycleOwner) { school ->
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
            if (studentId in OVERLAP_MIN_ID..OVERLAP_MAX_ID) {
                binding.tvStudentidSearch.text = ""
            } else {
                binding.tvStudentidSearch.text = getString(R.string.onboarding_student_id, studentId)
            }
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnUniversityInfoNext.setOnSingleClickListener {
            amplitudeUniversityInfo()
            findNavController().navigate(R.id.action_universityInfoFragment_to_yelIoIdFragment)
            val activity = requireActivity() as OnBoardingActivity
            activity.progressBarPlus()
        }
    }

    private fun amplitudeUniversityInfo() {
        AmplitudeUtils.trackEventWithProperties(
            EVENT_CLICK_ONBOARDING_NEXT,
            JSONObject().put(NAME_ONBOARD_VIEW, VALUE_SCHOOL),
        )
        AmplitudeUtils.updateUserProperties(PROPERTY_USER_SCHOOL, viewModel.university)
        AmplitudeUtils.updateUserProperties(PROPERTY_USER_DEPARTMENT, viewModel.departmentText.value.toString())
        AmplitudeUtils.updateUserIntProperties(PROPERTY_USER_GRADE, viewModel.studentId)
    }
    companion object {
        private const val OVERLAP_MIN_ID = 1
        private const val OVERLAP_MAX_ID = 3
        private const val EVENT_CLICK_ONBOARDING_NEXT = "click_onboarding_next"
        private const val NAME_ONBOARD_VIEW = "onboard_view"
        private const val VALUE_SCHOOL = "school"
        private const val PROPERTY_USER_SCHOOL = "user_school"
        private const val PROPERTY_USER_DEPARTMENT = "user_department"
        private const val PROPERTY_USER_GRADE = "user_grade"
    }
}
