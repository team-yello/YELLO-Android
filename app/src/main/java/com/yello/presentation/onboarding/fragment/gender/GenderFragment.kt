package com.yello.presentation.onboarding.fragment.gender

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentGenderBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class GenderFragment : BindingFragment<FragmentGenderBinding>(R.layout.fragment_gender) {

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setConfirmBtnClickListener()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnGenderNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
    }
}
