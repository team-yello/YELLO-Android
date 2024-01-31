package com.el.yello.presentation.main.profile.mod

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentModSelectBottomSheetBinding
import com.el.yello.presentation.onboarding.fragment.universityinfo.studentid.StudentIdDialogAdapter
import com.example.ui.base.BindingBottomSheetDialog

class UnivModSelectBottomSheet :
    BindingBottomSheetDialog<FragmentModSelectBottomSheetBinding>(R.layout.fragment_mod_select_bottom_sheet) {

    private val viewModel by activityViewModels<UnivProfileModViewModel>()

    private var _adapter: StudentIdDialogAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setStudentIdList()
    }

    private fun initAdapter() {
        _adapter = StudentIdDialogAdapter(storeStudentId = ::storeStudentId)
        binding.rvSelectList.adapter = adapter
    }

    private fun storeStudentId(studentId: Int) {
        if (viewModel.admYear.value != studentId.toString()) {
            viewModel.admYear.value = studentId.toString()
            viewModel.isChanged = true
        }
        dismiss()
    }

    private fun setStudentIdList() {
        adapter.submitList(viewModel.studentIdList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = UnivModSelectBottomSheet()
    }

}