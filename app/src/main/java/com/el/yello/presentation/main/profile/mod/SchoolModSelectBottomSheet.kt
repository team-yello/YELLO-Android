package com.el.yello.presentation.main.profile.mod

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentModSelectBottomSheetBinding
import com.el.yello.presentation.onboarding.fragment.highschoolinfo.group.GroupDialogAdapter
import com.example.ui.base.BindingBottomSheetDialog

class SchoolModSelectBottomSheet :
    BindingBottomSheetDialog<FragmentModSelectBottomSheetBinding>(R.layout.fragment_mod_select_bottom_sheet) {

    private val viewModel by activityViewModels<SchoolProfileModViewModel>()

    private var _classAdapter: GroupDialogAdapter? = null
    private val classAdapter
        get() = requireNotNull(_classAdapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private var isGradeSearch = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIsUnivSearch()
        initAdapter()
        setClassroomList()
    }

    private fun checkIsUnivSearch() {
        isGradeSearch = arguments?.getBoolean(ARGS_IS_GRADE_SEARCH) ?: true
    }

    private fun initAdapter() {
        _classAdapter = GroupDialogAdapter(storeHighSchoolGroup = ::storeHighSchoolGroup)
        binding.rvSelectList.adapter = classAdapter
    }

    private fun storeHighSchoolGroup(classroom: String) {
        if (viewModel.classroom.value != classroom) {
            viewModel.classroom.value = classroom
            viewModel.isChanged = true
        }
        dismiss()
    }

    private fun setClassroomList() {
        classAdapter.submitList(viewModel.classroomList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _classAdapter = null
    }

    companion object {
        @JvmStatic
        fun newInstance(isGradeSearch: Boolean) = SchoolModSelectBottomSheet().apply {
            arguments = bundleOf(ARGS_IS_GRADE_SEARCH to isGradeSearch)
        }

        private const val ARGS_IS_GRADE_SEARCH = "IS_GRADE_SEARCH"
    }
}