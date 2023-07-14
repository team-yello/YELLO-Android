package com.yello.presentation.onboarding.fragment.studentid.dialog.studentid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.MyStudentid
import com.example.ui.base.BindingBottomSheetDialog
import com.yello.R
import com.yello.databinding.FragmentDialogStudentIdBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class StudentidDialogFragment :
    BindingBottomSheetDialog<FragmentDialogStudentIdBinding>(R.layout.fragment_dialog_student_id) {

    private lateinit var idList: List<MyStudentid>

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStudentidAdapter()
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun initStudentidAdapter() {
        viewModel.addStudentId()
        idList = viewModel.studentidResult.value ?: emptyList()
        val adapter = StudentidDialogAdapter(requireContext())
        binding.rvStudentid.adapter = adapter
        adapter.submitList(idList)
    }

    companion object {
        @JvmStatic
        fun newInstance() = StudentidDialogFragment()
    }
}
