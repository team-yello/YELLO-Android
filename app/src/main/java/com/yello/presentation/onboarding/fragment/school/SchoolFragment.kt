package com.yello.presentation.onboarding.fragment.school

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentSchoolBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.yello.presentation.onboarding.fragment.school.university.SearchDialogFragment

class SchoolFragment : BindingFragment<FragmentSchoolBinding>(R.layout.fragment_school) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initSearchSchoolBtnClickListener()
        setConfirmBtnClickListener()
        setupSchool()
        binding.tvSchoolSearch.doAfterTextChanged {
            it.toString()
        }
    }

    private fun initSearchSchoolBtnClickListener() {
        binding.tvSchoolSearch.setOnSingleClickListener {
            SearchDialogFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnSchoolNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
    }

    private fun setupSchool() {
        viewModel._school.observe(viewLifecycleOwner) { school ->
            binding.tvSchoolSearch.text = school
        }
    }
}
