package com.yello.presentation.onboarding.fragment.studentid.dialog.department

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.MyDepartment
import com.example.ui.base.BindingBottomSheetDialog
import com.yello.R
import com.yello.databinding.FragmentDialogDepartmentBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class SearchDialogDepartmentFragment :
    BindingBottomSheetDialog<FragmentDialogDepartmentBinding>(R.layout.fragment_dialog_department) {

    private lateinit var departmentList: List<MyDepartment>
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDepartmentAdapter()
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun initDepartmentAdapter() {
        viewModel.addDepartment()
        departmentList = viewModel.departmentResult.value ?: emptyList()
        val adapter = DepartmentAdapter(requireContext())
        binding.rvDepartmentList.adapter = adapter
        adapter.submitList(departmentList)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogDepartmentFragment()
    }
}
