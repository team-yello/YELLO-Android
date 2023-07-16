package com.yello.presentation.onboarding.fragment.school.university

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.MySchool
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentDialogSchoolBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.yello.presentation.onboarding.fragment.school.SchoolAdapter

class SearchDialogFragment :
    BindingBottomSheetDialog<FragmentDialogSchoolBinding>(R.layout.fragment_dialog_school) {
    private lateinit var schoolList: List<MySchool>

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initSchoolAdapter()
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun initSchoolAdapter() {
        viewModel.addSchool()
        schoolList = viewModel.schoolResult.value ?: emptyList()
        val adapter = SchoolAdapter(requireContext(), storeSchool = ::storeSchool)
        binding.rvSchoolList.adapter = adapter
        adapter.submitList(schoolList)
    }

    fun storeSchool(school: String) {
        viewModel.setSchool(school)
        dismiss()
        binding.layoutSchoolDialog.setOnSingleClickListener {
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogFragment()
    }
}
