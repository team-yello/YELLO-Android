package com.yello.presentation.onboarding.fragment.school.university

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.ui.base.BindingBottomSheetDialog
import com.yello.R
import com.yello.databinding.FragmentDialogSchoolBinding
import com.yello.presentation.onboarding.fragment.school.SchoolAdapter
import com.yello.presentation.onboarding.fragment.school.SchoolViewModel

class SearchDialogFragment :
    BindingBottomSheetDialog<FragmentDialogSchoolBinding>(R.layout.fragment_dialog_school) {

    private var schoolAdapter: SchoolAdapter? = null
    private val viewModel by viewModels<SchoolViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSchoolAdapter()
    }
    private fun initSchoolAdapter() {
        binding.rvSchoolList.adapter = schoolAdapter
        viewModel.addSchool()
        schoolAdapter?.submitList(viewModel.schoolResult.value)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogFragment()
    }
}
