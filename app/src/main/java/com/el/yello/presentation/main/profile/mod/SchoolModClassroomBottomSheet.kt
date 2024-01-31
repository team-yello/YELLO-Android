package com.el.yello.presentation.main.profile.mod

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentModSelectBottomSheetBinding
import com.el.yello.presentation.onboarding.fragment.highschoolinfo.group.GroupDialogAdapter
import com.example.ui.base.BindingBottomSheetDialog

class SchoolModClassroomBottomSheet :
    BindingBottomSheetDialog<FragmentModSelectBottomSheetBinding>(R.layout.fragment_mod_select_bottom_sheet) {

    private val viewModel by activityViewModels<SchoolProfileModViewModel>()

    private var _adapter: GroupDialogAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setClassroomList()
    }

    private fun initAdapter() {
        _adapter = GroupDialogAdapter(storeHighSchoolGroup = ::storeHighSchoolGroup)
        binding.rvSelectList.adapter = adapter
    }

    private fun storeHighSchoolGroup(classroom: String) {
        if (viewModel.classroom.value != classroom) {
            viewModel.classroom.value = classroom
            viewModel.isChanged = true
        }
        dismiss()
    }

    private fun setClassroomList() {
        adapter.submitList(viewModel.classroomList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SchoolModClassroomBottomSheet()
    }
}