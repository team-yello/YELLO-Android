package com.el.yello.presentation.onboarding.fragment.gender

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentSelectStudentTypeBinding
import com.el.yello.presentation.auth.SocialSyncActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.example.domain.enum.StudentTypeEnum
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class SelectStudentFragment :
    BindingFragment<FragmentSelectStudentTypeBinding>(R.layout.fragment_select_student_type) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.highschool = StudentTypeEnum.H.toString()
        binding.university = StudentTypeEnum.U.toString()

        setupStudentType()
        setConfirmBtnClickListener()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnGenderNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
        binding.btnSelectStudentTypeBack.setOnSingleClickListener {
            val intent = Intent(activity, SocialSyncActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupStudentType() {
        viewModel._studenttype.observe(viewLifecycleOwner) { studenttype ->
            when (studenttype) {
                StudentTypeEnum.H.toString() -> {
                    binding.btnSchoolHighschool.setBackgroundResource(R.drawable.shape_black_fill_yello_main_500_line_8_rect)
                    binding.btnSchoolUniversity.setBackgroundResource(R.drawable.shape_black_fill_grayscales700_line_8_rect)
                    binding.ivStudentHighschool.setImageResource(R.drawable.ic_student_highschool_face_select)
                    binding.ivStudentUniversity.setImageResource(R.drawable.ic_student_university_face_unselected)
                    binding.tvStudentHighschool.setTextColor(resources.getColor(R.color.yello_main_500))
                    binding.tvStudentUniversity.setTextColor(resources.getColor(R.color.grayscales_700))
                }

                StudentTypeEnum.U.toString() -> {
                    binding.btnSchoolUniversity.setBackgroundResource(R.drawable.shape_black_fill_yello_main_500_line_8_rect)
                    binding.btnSchoolHighschool.setBackgroundResource(R.drawable.shape_black_fill_grayscales700_line_8_rect)
                    binding.ivStudentUniversity.setImageResource(R.drawable.ic_student_university_face_select)
                    binding.ivStudentHighschool.setImageResource(R.drawable.ic_student_highschool_face_unselected)
                    binding.tvStudentUniversity.setTextColor(resources.getColor(R.color.yello_main_500))
                    binding.tvStudentHighschool.setTextColor(resources.getColor(R.color.grayscales_700))
                }
            }
        }
    }
}
