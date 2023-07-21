package com.el.yello.presentation.onboarding.fragment.school

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentSchoolBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.school.university.SearchDialogSchoolFragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.el.yello.presentation.auth.SocialSyncActivity

class SchoolFragment : BindingFragment<FragmentSchoolBinding>(R.layout.fragment_school) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initSearchSchoolBtnClickListener()
        setConfirmBtnClickListener()
        setupSchool()
        binding.btnSchoolBackBtn.setOnSingleClickListener {
            val intent = Intent(getActivity(), SocialSyncActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initSearchSchoolBtnClickListener() {
        binding.tvSchoolSearch.setOnSingleClickListener {
            SearchDialogSchoolFragment().show(parentFragmentManager, this.tag)
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
