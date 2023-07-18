package com.yello.presentation.onboarding.fragment.studentid.dialog.department

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.onboarding.MyDepartment
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentDialogDepartmentBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class SearchDialogDepartmentFragment :
    BindingBottomSheetDialog<FragmentDialogDepartmentBinding>(R.layout.fragment_dialog_department) {

    private lateinit var departmentList: List<MyDepartment>
    private var adapter: DepartmentAdapter? = null

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initDepartmentAdapter()
        setupDepartmentData()
    }

    private fun initDepartmentAdapter() {
        binding.etDepartmentSearch.doAfterTextChanged {
            viewModel.addListDepartment("서울대학교", it.toString())
        }
        departmentList = viewModel.departmentResult.value ?: emptyList()
        val adapter = DepartmentAdapter(requireContext(), storeDepartment = ::storeDepartment)
        binding.rvDepartmentList.adapter = adapter
    }

    private fun setupDepartmentData() {
        viewModel.departmentData.observe(viewLifecycleOwner) {
            adapter?.submitList(departmentList)
        }
    }

    fun storeDepartment(department: String) {
        viewModel.setDepartment(department)
        dismiss()
        binding.layoutDepartmentDialog.setOnSingleClickListener {
            dismiss()
        }
        binding.btnBackDialog.setOnSingleClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogDepartmentFragment()
    }
}
