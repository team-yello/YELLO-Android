package com.el.yello.presentation.onboarding.fragment.highschoolinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentHighschoolBinding
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.fragment.highschoolinfo.group.GroupDialogFragment
import com.el.yello.presentation.onboarding.fragment.highschoolinfo.school.SearchDialogHighSchoolFragment
import com.el.yello.util.manager.AmplitudeManager
import com.el.yello.util.extension.yelloSnackbar
import com.example.domain.enum.Grade
import com.example.ui.base.BindingFragment
import com.example.ui.extension.colorOf
import com.example.ui.extension.setOnSingleClickListener
import org.json.JSONObject

class HighSchoolInfoFragment :
    BindingFragment<FragmentHighschoolBinding>(R.layout.fragment_highschool) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        with(binding) {
            firstGrade = Grade.First.toInt()
            secondGrade = Grade.Second.toInt()
            thirdGrade = Grade.Third.toInt()
        }
        setupHighSchool()
        setupGrade()
        setupGroup()
        initSearchInfoBtnClickListener()
        setConfirmBtnClickListener()
    }

    override fun onResume() {
        super.onResume()
        callParentActivity {
            showBackBtn()
        }
    }

    private fun callParentActivity(callback: OnBoardingActivity.() -> Unit) {
        val activity = requireActivity()
        if (activity is OnBoardingActivity) {
            activity.callback()
        }
    }

    private fun initSearchInfoBtnClickListener() {
        binding.tvHighschoolSearch.setOnSingleClickListener {
            SearchDialogHighSchoolFragment().show(parentFragmentManager, this.tag)
        }
        binding.tvGroupSearch.setOnSingleClickListener {
            if (binding.tvHighschoolSearch.text.isNotBlank()) {
                GroupDialogFragment().show(parentFragmentManager, this.tag)
            } else {
                yelloSnackbar(
                    binding.root.rootView,
                    getString(R.string.onboarding_group_select_warning),
                )
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
                Grade.First.toInt() -> {
                    changeFirstGradeBtn()
                }

                Grade.Second.toInt() -> {
                    changeSecondGradeBtn()
                }

                Grade.Third.toInt() -> {
                    changeThirdGradeBtn()
                }
            }
        }
    }

    private fun setupGroup() {
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
            tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello600_line_rect)
            tvGradeFirst.setTextColor(binding.root.context.colorOf(R.color.yello_main_600))
            tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_rect)
            tvGradeSecond.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
            tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_right8_rect)
            tvGradeThird.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
        }
    }

    private fun changeSecondGradeBtn() {
        with(binding) {
            tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_left8_rect)
            tvGradeFirst.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
            tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello600_line_rect)
            tvGradeSecond.setTextColor(binding.root.context.colorOf(R.color.yello_main_600))
            tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_right8_rect)
            tvGradeThird.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
        }
    }

    private fun changeThirdGradeBtn() {
        with(binding) {
            tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_left8_rect)
            tvGradeFirst.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
            tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_rect)
            tvGradeSecond.setTextColor(binding.root.context.colorOf(R.color.grayscales_700))
            tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello600_line_right8_rect)
            tvGradeThird.setTextColor(binding.root.context.colorOf(R.color.yello_main_600))
        }
    }

    private fun amplitudeHighSchoolInfo() {
        with(AmplitudeManager) {
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
