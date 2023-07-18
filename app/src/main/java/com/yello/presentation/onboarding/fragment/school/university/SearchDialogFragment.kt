package com.yello.presentation.onboarding.fragment.school.university

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.onboarding.MySchool
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentDialogSchoolBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.yello.presentation.onboarding.fragment.school.SchoolAdapter

class SearchDialogFragment :
    BindingBottomSheetDialog<FragmentDialogSchoolBinding>(R.layout.fragment_dialog_school) {
    private lateinit var schoolList: List<MySchool>
    private var adapter: SchoolAdapter? = null

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initView()
        observe()
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun initView() {
        binding.etSchoolSearch.doAfterTextChanged {
            viewModel.addListSchool(it.toString())
        }
        schoolList = viewModel.schoolResult.value ?: emptyList()
        adapter = SchoolAdapter(requireContext(), storeSchool = ::storeSchool)
        binding.rvSchoolList.adapter = adapter
    }

    private fun observe() {
        viewModel.schoolData.observe(viewLifecycleOwner) {
            adapter?.submitList(schoolList)
        }
    }

    fun storeSchool(school: String) {
        viewModel.setSchool(school)
        dismiss()
        binding.layoutSchoolDialog.setOnSingleClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogFragment()
    }
}
