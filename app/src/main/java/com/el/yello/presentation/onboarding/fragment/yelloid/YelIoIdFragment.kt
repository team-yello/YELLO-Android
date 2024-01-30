package com.el.yello.presentation.onboarding.fragment.yelloid

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloIdBinding
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
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
        setYelloIdBtnClickListener()
        observeGetValidYelloIdState()
    }

    private fun setYelloIdBtnClickListener() {
        binding.btnYelloIdNext.setOnSingleClickListener {
            amplitudeYelloIdInfo()
            viewModel.getValidYelloId(viewModel.id)
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnIdDelete.setOnClickListener {
            binding.etId.text.clear()
        }
    }

    private fun observeGetValidYelloIdState() {
        viewModel.getValidYelloIdState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data) {
                        initIdEditTextViewError()
                        return@observe
                    }
                    viewModel.resetGetValidYelloId()
                    findNavController().navigate(R.id.action_yelIoIdFragment_to_addFriendFragment)
                    val activity = requireActivity() as OnBoardingActivity
                    activity.progressBarPlus()
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

    private fun amplitudeYelloIdInfo() {
        AmplitudeUtils.trackEventWithProperties(
            EVENT_CLICK_ONBOARDING_NEXT,
            JSONObject().put(NAME_ONBOARD_VIEW, VALUE_ID),
        )
        AmplitudeUtils.updateUserProperties(PROPERTY_USER_ID, viewModel.id)
    }

    companion object {
        private const val EVENT_CLICK_ONBOARDING_NEXT = "click_onboarding_next"
        private const val NAME_ONBOARD_VIEW = "onboard_view"
        private const val VALUE_ID = "id"
        private const val PROPERTY_USER_ID = "user_id"
    }
}
