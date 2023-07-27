package com.el.yello.presentation.onboarding.fragment.school

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentSchoolBinding
import com.el.yello.presentation.auth.SocialSyncActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.school.dialog.SearchDialogSchoolFragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class SchoolFragment : BindingFragment<FragmentSchoolBinding>(R.layout.fragment_school) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initSearchSchoolBtnClickListener()
        setConfirmBtnClickListener()
        setupSchool()
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
        binding.btnSchoolBackBtn.setOnSingleClickListener {
            val intent = Intent(activity, SocialSyncActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSchool() {
        viewModel._school.observe(viewLifecycleOwner) { school ->
            binding.tvSchoolSearch.text = school
        }
    }
}
