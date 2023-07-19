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
                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }
                is UiState.Success -> {
                    if (state.data) {
                        viewModel.navigateToNextPage()
                        return@observe
                    }
                    initIdEditTextViewError()
                }
                is UiState.Failure -> {
                    if (state.msg == "404") {
                        viewModel.navigateToNextPage()
                        return@observe
                    }

                    yelloSnackbar(binding.root, getString(R.string.msg_error))
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

    private fun initIdEditTextViewError() {
        binding.etCode.setBackgroundResource(R.drawable.shape_fill_red20_line_semantic_status_red500_rect_8)
        binding.btnCodeDelete.setBackgroundResource(R.drawable.ic_onboarding_delete_red)
        binding.tvIdError.text = getString(R.string.name_id_duplicate_id_msg)
        binding.tvIdError.setTextColor(resources.getColor(R.color.semantic_red_500))
    }
}
