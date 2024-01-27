package com.el.yello.presentation.onboarding.fragment.highschoolinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentHighschoolBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.highschoolinfo.group.GroupDialogFragment
import com.el.yello.presentation.onboarding.fragment.highschoolinfo.school.SearchDialogHighSchoolFragment
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.enum.Grade
import com.example.ui.base.BindingFragment
import com.example.ui.context.colorOf
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class HighSchoolInfoFragment :
    BindingFragment<FragmentHighschoolBinding>(R.layout.fragment_highschool) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        with(binding) {
            first = Grade.A.toInt()
            second = Grade.B.toInt()
            third = Grade.C.toInt()
        }
        setupHighSchool()
        setupGrade()
        setupHighSchoolGroup()
        initSearchInfoBtnClickListener()
        setConfirmBtnClickListener()
    }

    override fun onResume() {
        super.onResume()
        (activity as? OnBoardingActivity)?.showBackBtn()
    }

    private fun initSearchInfoBtnClickListener() {
        binding.tvHighschoolSearch.setOnSingleClickListener {
            SearchDialogHighSchoolFragment().show(parentFragmentManager, this.tag)
        }
        binding.tvGroupSearch.setOnSingleClickListener {
            if (binding.tvHighschoolSearch.text.isNotBlank()) {
                GroupDialogFragment().show(parentFragmentManager, this.tag)
            } else {
                yelloSnackbar(binding.root.rootView, "학교와 학년을 선택해야 반을 선택할 수 있어요!")
            }
        }
    }

    private fun setupHighSchool() {
        viewModel.highSchoolText.observe(viewLifecycleOwner) { school ->
            binding.tvHighschoolSearch.text = school
        }
    }

    private fun setupGrade() {
        viewModel.studentIdText.observe(viewLifecycleOwner) { grade ->
            when (grade) {
                Grade.A.toInt() -> {
                    changeFirstGradeBtn()
                }

                Grade.B.toInt() -> {
                    changeSecondGradeBtn()
                }

                Grade.C.toInt() -> {
                    changeThirdGradeBtn()
                }
            }
        }
    }

    private fun setupHighSchoolGroup() {
        viewModel.highSchoolGroupText.observe(viewLifecycleOwner) { group ->
            binding.tvGroupSearch.text = getString(R.string.onboarding_group, group)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnHighschoolinfoNextBtn.setOnSingleClickListener {
            findNavController().navigate(R.id.action_highschoolInfoFragment_to_yelIoIdFragment)
            amplitudeHighSchoolInfo()
            val activity = requireActivity() as OnBoardingActivity
            activity.progressBarPlus()
        }
    }

    private fun changeFirstGradeBtn() {
        with(binding) {
            tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello_main600_line_8_leftrect)
            tvGradeFirst.setTextColor(binding.root.context.colorOf(R.color.yello_main_600))
            tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_square)
            tvGradeSecond.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
            tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_rightrect)
            tvGradeThird.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
        }
    }

    private fun changeSecondGradeBtn() {
        with(binding) {
            tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_leftrect)
            tvGradeFirst.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
            tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello_main600_line_8_square)
            tvGradeSecond.setTextColor(binding.root.context.colorOf(R.color.yello_main_600))
            tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_rightrect)
            tvGradeThird.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
        }
    }

    private fun changeThirdGradeBtn() {
        with(binding) {
            tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_leftrect)
            tvGradeFirst.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
            tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_square)
            tvGradeSecond.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
            tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello_main600_line_8_rightrect)
            tvGradeThird.setTextColor(binding.root.context.colorOf(R.color.yello_main_600))
        }
    }

    private fun amplitudeHighSchoolInfo() {
        with(AmplitudeUtils) {
            trackEventWithProperties(
                EVENT_CLICK_ONBOARDING_NEXT,
                JSONObject().put(NAME_ONBOARD_VIEW, VALUE_SCHOOL),
            )
            updateUserProperties(PROPERTY_USER_SCHOOL, viewModel.highSchool)
            updateUserProperties(
                PROPERTY_USER_DEPARTMENT,
                viewModel.highSchoolGroupText.value.toString(),
            )
            updateUserIntProperties(PROPERTY_USER_GRADE, viewModel.studentId)
        }
    }

    companion object {
        private const val EVENT_CLICK_ONBOARDING_NEXT = "click_onboarding_next"
        private const val NAME_ONBOARD_VIEW = "onboard_view"
        private const val VALUE_SCHOOL = "school"
        private const val PROPERTY_USER_SCHOOL = "user_school"
        private const val PROPERTY_USER_DEPARTMENT = "user_department"
        private const val PROPERTY_USER_GRADE = "user_grade"
    }
}
