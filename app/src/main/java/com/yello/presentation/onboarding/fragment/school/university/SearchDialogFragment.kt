package com.yello.presentation.onboarding.fragment.school.university

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.domain.entity.MySchool
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentDialogSchoolBinding
import com.yello.presentation.onboarding.fragment.school.SchoolAdapter
import com.yello.presentation.onboarding.fragment.school.SchoolViewModel

class SearchDialogFragment :
    BindingBottomSheetDialog<FragmentDialogSchoolBinding>(R.layout.fragment_dialog_school) {

    private lateinit var schoolList: List<MySchool>

    private val viewModel by viewModels<SchoolViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSchoolAdapter()
    }

    private fun initSchoolAdapter() {
        viewModel.addSchool()
        schoolList = viewModel.schoolResult.value ?: emptyList()
        val adapter = SchoolAdapter(requireContext())
        binding.rvSchoolList.adapter = adapter
        adapter.submitList(schoolList)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogFragment()
    }
}
