package com.el.yello.presentation.main.profile.schoolmod

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentModGradeBottomSheetBinding
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener

class SchoolModGradeBottomSheet :
    BindingBottomSheetDialog<FragmentModGradeBottomSheetBinding>(R.layout.fragment_mod_grade_bottom_sheet) {

    private val viewModel by activityViewModels<SchoolProfileModViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGradeBtnListener()
    }

    private fun initGradeBtnListener() {
        binding.tvItemGradeOne.setOnSingleClickListener { setGrade(1) }
        binding.tvItemGradeTwo.setOnSingleClickListener { setGrade(2) }
        binding.tvItemGradeThree.setOnSingleClickListener { setGrade(3) }
    }

    private fun setGrade(grade: Int) {
        if (viewModel.grade.value != grade.toString()) {
            viewModel.grade.value = grade.toString()
            viewModel.isChanged = true
            (activity as SchoolProfileModActivity).checkIsGradeTextNone()
        }
        dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SchoolModGradeBottomSheet()
    }
}