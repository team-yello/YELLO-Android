package com.yello.presentation.onboarding.fragment.code

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentCodeBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class CodeFragment : BindingFragment<FragmentCodeBinding>(R.layout.fragment_code) {

    private val viewModel by activityViewModels<OnBoardingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setConfirmBtnClickListener()
        setConfirmBtnCLickListener()
        setDeleteCodeBtnClickListener()
    }
    private fun setConfirmBtnClickListener() {
        binding.btnCodeNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
    }

    private fun setConfirmBtnCLickListener() {
        binding.btnCodeSkip.setOnClickListener {
            viewModel.navigateToNextPage()
        }
    }

    private fun setDeleteCodeBtnClickListener() {
        binding.btnCodeDelete.setOnSingleClickListener {
            val editcode = binding.etCode
            editcode.setText("")
        }
    }
}
