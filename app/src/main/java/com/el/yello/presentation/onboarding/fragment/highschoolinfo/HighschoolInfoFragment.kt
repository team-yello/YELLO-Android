package com.el.yello.presentation.onboarding.fragment.highschoolinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentHighschoolBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.highschoolinfo.group.GroupDialogFragment
import com.el.yello.presentation.onboarding.fragment.highschoolinfo.school.SearchDialogHighSchoolFragment
import com.example.domain.enum.GradeEnum
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import java.util.Timer
import kotlin.concurrent.timer

class HighschoolInfoFragment :
    BindingFragment<FragmentHighschoolBinding>(R.layout.fragment_highschool) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 16
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        progressBarTimerFun()

        binding.first = GradeEnum.A.toString()
        binding.second = GradeEnum.B.toString()
        binding.third = GradeEnum.C.toString()
        setupGrade()
        setupGroup()
        initSearchInfoBtnClickListener()
        setConfirmBtnClickListener()
    }

    private fun progressBarTimerFun() {
        binding.highschoolProgressbar.progress = 16
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 8, initialDelay = 300) {
            if (deltaTime > 32) cancel()
            binding.highschoolProgressbar.setProgress(++deltaTime)
            println(binding.highschoolProgressbar.progress)
        }
    }

    private fun initSearchInfoBtnClickListener() {
        binding.tvHighschoolSearch.setOnSingleClickListener {
            SearchDialogHighSchoolFragment().show(parentFragmentManager, this.tag)
        }
        binding.tvGroupSearch.setOnSingleClickListener {
            GroupDialogFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun setupGrade() {
        viewModel.gradeText.observe(viewLifecycleOwner) { grade ->
            when (grade) {
                GradeEnum.A.toString() -> {
                    binding.tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello_main600_line_8_leftrect)
                    binding.tvGradeFirst.setTextColor(resources.getColor(R.color.yello_main_600))
                    binding.tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_square)
                    binding.tvGradeSecond.setTextColor(resources.getColor(R.color.grayscales_700))
                    binding.tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_rightrect)
                    binding.tvGradeThird.setTextColor(resources.getColor(R.color.grayscales_700))
                }

                GradeEnum.B.toString() -> {
                    binding.tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_leftrect)
                    binding.tvGradeFirst.setTextColor(resources.getColor(R.color.grayscales_700))
                    binding.tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello_main600_line_8_square)
                    binding.tvGradeSecond.setTextColor(resources.getColor(R.color.yello_main_600))
                    binding.tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_rightrect)
                    binding.tvGradeThird.setTextColor(resources.getColor(R.color.grayscales_700))
                }

                GradeEnum.C.toString() -> {
                    binding.tvGradeFirst.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_leftrect)
                    binding.tvGradeFirst.setTextColor(resources.getColor(R.color.grayscales_700))
                    binding.tvGradeSecond.setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales700_line_8_square)
                    binding.tvGradeSecond.setTextColor(resources.getColor(R.color.grayscales_700))
                    binding.tvGradeThird.setBackgroundResource(R.drawable.shape_grayscales900_fill_yello_main600_line_8_rightrect)
                    binding.tvGradeThird.setTextColor(resources.getColor(R.color.yello_main_600))
                }
            }
        }
    }

    private fun setupGroup() {
        viewModel.groupText.observe(viewLifecycleOwner) { group ->
            binding.tvGroupSearch.text = getString(R.string.onboarding_group, group)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnHighschoolinfoNextBtn.setOnSingleClickListener {
           // findNavController().navigate(R.id.action_highschoolInfoFragment_to_genderFragment)
        }
        binding.btnHighschoolinfoBackBtn.setOnSingleClickListener {
           // findNavController().navigate(R.id.action_highschoolInfoFragment_to_selectStudentFragment)
        }
    }
}
