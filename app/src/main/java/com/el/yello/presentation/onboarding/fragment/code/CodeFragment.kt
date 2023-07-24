package com.el.yello.presentation.onboarding.fragment.code

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentCodeBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState

class CodeFragment : BindingFragment<FragmentCodeBinding>(R.layout.fragment_code) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setConfirmBtnCLickListener()
        setDeleteIdBtnClickListener()
        setupPostSignupState()
        viewModel.validYellIdLoading()
    }

    override fun onResume() {
        super.onResume()

        setupGetValidYelloIdState()
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

    private fun setupGetValidYelloIdState() {
        viewModel.getValidYelloId.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data) {
                        initIdEditTextViewError()
                        return@observe
                    }
                    viewModel.postSignup()
                }

                is UiState.Failure -> {
                    if (state.msg == "404") {
                        viewModel.postSignup()
                        return@observe
                    }
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {}
            }
        }
    }

    private fun setDeleteIdBtnClickListener() {
        binding.btnCodeDelete.setOnClickListener {
            binding.etCode.setText("")
        }
    }

    private fun initIdEditTextViewError() {
        binding.etCode.setBackgroundResource(R.drawable.shape_fill_red20_line_semantic_status_red500_rect_8)
        binding.btnCodeDelete.setBackgroundResource(R.drawable.ic_onboarding_delete_red)
        binding.tvIdError.text = getString(R.string.code_duplicate_msg)
        binding.tvIdError.setTextColor(resources.getColor(R.color.semantic_red_500))
    }
}
