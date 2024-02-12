package com.el.yello.presentation.onboarding.fragment.studenttype

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentSelectStudentTypeBinding
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.enum.StudentType
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.colorOf
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class SelectStudentFragment :
    BindingFragment<FragmentSelectStudentTypeBinding>(R.layout.fragment_select_student_type) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.highschool = StudentType.HIGHSCHOOL.toString()
        binding.university = StudentType.UNIVERSITY.toString()
        setupStudentType()
    }

    override fun onResume() {
        super.onResume()
        (activity as? OnBoardingActivity)?.hideBackBtn()
    }
    private fun setupStudentType() {
        viewModel.studentType.observe(viewLifecycleOwner) { studentType ->
            when (studentType) {
                StudentType.HIGHSCHOOL.toString() -> {
                    changeHighSchoolBtn()
                    binding.btnSelectTypeNext.setOnSingleClickListener {
                        findNavController().navigate(R.id.action_selectStudentFragment_to_highschoolInfoFragment)
                        amplitudeSelectStudent()
                        AmplitudeUtils.updateUserProperties(EVENT_STUDENT_TYPE, VALUE_HIGH_SCHOOL)
                        val activity = requireActivity() as OnBoardingActivity
                        activity.progressBarPlus()
                    }
                }

                StudentType.UNIVERSITY.toString() -> {
                    changeUniversityBtn()
                    binding.btnSelectTypeNext.setOnSingleClickListener {
                        findNavController().navigate(R.id.action_selectStudentFragment_to_universityInfoFragment)
                        amplitudeSelectStudent()
                        AmplitudeUtils.updateUserProperties(EVENT_STUDENT_TYPE, VALUE_UNIVERSITY)
                        val activity = requireActivity() as OnBoardingActivity
                        activity.progressBarPlus()
                    }
                }
            }
        }
    }

    private fun changeHighSchoolBtn() {
        with(binding) {
            btnSchoolHighschool.setBackgroundResource(R.drawable.shape_black_fill_yello_main_500_line_8_rect)
            btnSchoolUniversity.setBackgroundResource(R.drawable.shape_black_fill_grayscales700_line_8_rect)
            ivStudentHighschool.setImageResource(R.drawable.ic_student_highschool_face_select)
            ivStudentUniversity.setImageResource(R.drawable.ic_student_university_face_unselected)
            tvStudentHighschool.setTextColor(colorOf(R.color.yello_main_500))
            tvStudentUniversity.setTextColor(colorOf(R.color.grayscales_700))
        }
    }

    private fun changeUniversityBtn() {
        with(binding) {
            btnSchoolUniversity.setBackgroundResource(R.drawable.shape_black_fill_yello_main_500_line_8_rect)
            btnSchoolHighschool.setBackgroundResource(R.drawable.shape_black_fill_grayscales700_line_8_rect)
            ivStudentUniversity.setImageResource(R.drawable.ic_student_university_face_select)
            ivStudentHighschool.setImageResource(R.drawable.ic_student_highschool_face_unselected)
            tvStudentUniversity.setTextColor(colorOf(R.color.yello_main_500))
            tvStudentHighschool.setTextColor(colorOf(R.color.grayscales_700))
        }
    }

    private fun amplitudeSelectStudent() {
        AmplitudeUtils.trackEventWithProperties(
            "click_onboarding_next",
            JSONObject().put("onboard_view", "student_type"),
        )
    }

    companion object {
        private const val EVENT_STUDENT_TYPE = "user_student_type"
        private const val VALUE_HIGH_SCHOOL = "highschool"
        private const val VALUE_UNIVERSITY = "university"
    }
}
