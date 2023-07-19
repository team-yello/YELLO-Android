package com.yello.presentation.onboarding.fragment.code

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentCodeBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.yello.util.context.yelloSnackbar

class CodeFragment : BindingFragment<FragmentCodeBinding>(R.layout.fragment_code) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setConfirmBtnCLickListener()
        setDeleteCodeBtnClickListener()
        setDeleteIdBtnClickListener()
        setupPostSignupState()
    }

    private fun setConfirmBtnCLickListener() {
        binding.btnCodeSkip.setOnClickListener {
            viewModel.postSignup()
        }
    }

    private fun setupPostSignupState() {
        viewModel.postSignupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }
                is UiState.Empty -> {}
                is UiState.Success -> {
                    viewModel.navigateToNextPage()
                }
            }
        }
    }

    private fun setDeleteCodeBtnClickListener() {
        binding.btnCodeDelete.setOnSingleClickListener {
            val editcode = binding.etCode
            editcode.setText("")
        }
    }



    private fun setDeleteIdBtnClickListener() {
        binding.btnCodeDelete.setOnClickListener {
            val editcode = binding.etCode
            editcode.setText("")
        }
    }
}
