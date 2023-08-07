package com.el.yello.presentation.onboarding.fragment.nameid

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentNameIdBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener

class NameIdFragment : BindingFragment<FragmentNameIdBinding>(R.layout.fragment_name_id) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setDeleteBtnClickListener()
        setConfirmBtnClickListener()
        setupGetValidYelloId()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnNameidNext.setOnSingleClickListener {
            viewModel.getValidYelloId(viewModel.id)
        }
        binding.btnNameidBackBtn.setOnSingleClickListener {
            viewModel.navigateToBackPage()
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnNameDelete.setOnSingleClickListener {
            binding.etName.text.clear()
        }
        binding.btnIdDelete.setOnClickListener {
            binding.etId.text.clear()
        }
    }

    private fun setupGetValidYelloId() {
        viewModel.getValidYelloId.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data) {
                        initIdEditTextViewError()
                        return@observe
                    }
                    viewModel.navigateToNextPage()
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }
            }
        }
    }

    private fun initIdEditTextViewError() {
        binding.etId.setBackgroundResource(R.drawable.shape_fill_red20_line_semantic_status_red500_rect_8)
        binding.btnIdDelete.setBackgroundResource(R.drawable.ic_onboarding_delete_red)
        binding.tvIdError.text = getString(R.string.onboarding_name_id_duplicate_id_msg)
        binding.tvIdError.setTextColor(ContextCompat.getColor(requireContext(), R.color.semantic_red_500))
    }
}
