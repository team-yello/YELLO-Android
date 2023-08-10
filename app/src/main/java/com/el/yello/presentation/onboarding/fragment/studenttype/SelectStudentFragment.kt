package com.el.yello.presentation.onboarding.fragment.gender

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentSelectStudentTypeBinding
import com.el.yello.presentation.auth.SocialSyncActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.example.domain.enum.StudentTypeEnum
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import java.util.Timer
import kotlin.concurrent.timer

class SelectStudentFragment :
    BindingFragment<FragmentSelectStudentTypeBinding>(R.layout.fragment_select_student_type) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.highschool = StudentTypeEnum.H.toString()
        binding.university = StudentTypeEnum.U.toString()

        progressBarTimerFun()
        setupStudentType()
        setConfirmBtnClickListener()
    }

    private fun progressBarTimerFun() {
        binding.studenttypeProgressbar.progress = 0
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 8, initialDelay = 300) {
            if (deltaTime > 16) cancel()
            binding.studenttypeProgressbar.setProgress(++deltaTime)
            println(binding.studenttypeProgressbar.progress)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnSelectTypeBack.setOnSingleClickListener {
            val intent = Intent(activity, SocialSyncActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupStudentType() {
        viewModel.studentType.observe(viewLifecycleOwner) { studenttype ->
            when (studenttype) {
                StudentTypeEnum.H.toString() -> {
                    binding.btnSchoolHighschool.setBackgroundResource(R.drawable.shape_black_fill_yello_main_500_line_8_rect)
                    binding.btnSchoolUniversity.setBackgroundResource(R.drawable.shape_black_fill_grayscales700_line_8_rect)
                    binding.ivStudentHighschool.setImageResource(R.drawable.ic_student_highschool_face_select)
                    binding.ivStudentUniversity.setImageResource(R.drawable.ic_student_university_face_unselected)
                    binding.tvStudentHighschool.setTextColor(resources.getColor(R.color.yello_main_500))
                    binding.tvStudentUniversity.setTextColor(resources.getColor(R.color.grayscales_700))
                    binding.btnSelectTypeNext.setOnSingleClickListener {
                        findNavController().navigate(R.id.action_selectStudentFragment_to_highschoolInfoFragment)
                    }
                }

                StudentTypeEnum.U.toString() -> {
                    binding.btnSchoolUniversity.setBackgroundResource(R.drawable.shape_black_fill_yello_main_500_line_8_rect)
                    binding.btnSchoolHighschool.setBackgroundResource(R.drawable.shape_black_fill_grayscales700_line_8_rect)
                    binding.ivStudentUniversity.setImageResource(R.drawable.ic_student_university_face_select)
                    binding.ivStudentHighschool.setImageResource(R.drawable.ic_student_highschool_face_unselected)
                    binding.tvStudentUniversity.setTextColor(resources.getColor(R.color.yello_main_500))
                    binding.tvStudentHighschool.setTextColor(resources.getColor(R.color.grayscales_700))
                    binding.btnSelectTypeNext.setOnSingleClickListener {
                        findNavController().navigate(R.id.action_selectStudentFragment_to_universityInfoFragment)
                    }
                }
            }
        }
    }
}
