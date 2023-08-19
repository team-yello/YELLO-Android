package com.el.yello.presentation.onboarding.fragment.nameid

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloIdBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class YelIoIdFragment : BindingFragment<FragmentYelloIdBinding>(R.layout.fragment_yello_id) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        setDeleteBtnClickListener()
        setConfirmBtnClickListener()
        setupGetValidYelloId()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnYelloIdNext.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_onboarding_next",
                JSONObject().put("onboard_view", "id"),
            )
            viewModel.getValidYelloId(viewModel.id)
            findNavController().navigate(R.id.action_yelIoIdFragment_to_addFriendFragment)
            val activity = requireActivity() as OnBoardingActivity
            activity.progressBarPlus()
        }
    }

    private fun setDeleteBtnClickListener() {
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
                    findNavController().navigate(R.id.action_yelIoIdFragment_to_addFriendFragment)
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
        binding.tvIdErrorFirst.text = getString(R.string.onboarding_name_id_duplicate_id_msg)
        binding.tvIdErrorFirst.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.semantic_red_500,
            ),
        )
        binding.tvIdErrorSecond.visibility = View.INVISIBLE
        binding.tvIdErrorThird.visibility = View.INVISIBLE
    }
}
