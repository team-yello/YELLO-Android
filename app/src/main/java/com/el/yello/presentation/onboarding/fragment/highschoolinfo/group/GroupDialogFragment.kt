package com.el.yello.presentation.onboarding.fragment.highschoolinfo.group

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogGroupBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.example.ui.base.BindingBottomSheetDialog

class GroupDialogFragment :
    BindingBottomSheetDialog<FragmentDialogGroupBinding>(R.layout.fragment_dialog_group) {

    private lateinit var groupList: List<Int>

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initGroupAdapter()
    }

    private fun initGroupAdapter() {
        viewModel.addStudentId()
        //   groupList = viewModel.studentIdResult.value ?: emptyList()
        //   val adapter = StudentIdDialogAdapter(storeStudentId = ::storeStudentId)
        //  binding.rvGroup.adapter = adapter
        //   adapter.submitList(groupList)
    }

    // TODO
    /*
    fun storeStudentId(studentId: Int) {
        viewModel.setStudentId(studentId)
        dismiss()
    }
*/
    companion object {
        @JvmStatic
        fun newInstance() = GroupDialogFragment()
    }
}