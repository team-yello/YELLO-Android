package com.yello.presentation.onboarding.fragment.school

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
        initSearchSchoolBtnClickListener()
        binding.vm = viewModel
    }

    private fun initSearchSchoolBtnClickListener() {
        binding.tvSchoolSearch.setOnSingleClickListener {
            SearchDialogFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun initTransactionButton(view: View, fragment: Fragment) {
        view.setOnSingleClickListener {
            parentFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                replace(R.id.fcv_main, fragment)
            }.commit()
        }
    }
}
