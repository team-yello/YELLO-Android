package com.yello.presentation.onboarding.fragment.school

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingBottomSheetDialog
import com.yello.R
import com.yello.databinding.FragmentDialogSchoolBinding

class SearchDialogFragment : BindingBottomSheetDialog<FragmentDialogSchoolBinding>(R.layout.fragment_dialog_school) {

    private var schoolAdpapter: SchoolAdpapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSchoolAdapter()
    }

    private fun initSchoolAdapter() {
        schoolAdpapter = SchoolAdpapter()
        binding.rvSchoollist.adapter = schoolAdpapter
    }
    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogFragment()
    }
}
