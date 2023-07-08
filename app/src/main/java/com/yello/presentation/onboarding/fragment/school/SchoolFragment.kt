package com.yello.presentation.onboarding.fragment.school

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentSchoolBinding

class SchoolFragment : BindingFragment<FragmentSchoolBinding>(R.layout.fragment_school) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchSchoolBtnClickListener()
    }

    private fun initSearchSchoolBtnClickListener() {
        binding.etSchoolSearch.setOnSingleClickListener {
            SearchDialogFragment().show(parentFragmentManager, this.tag)
        }
    }
}
