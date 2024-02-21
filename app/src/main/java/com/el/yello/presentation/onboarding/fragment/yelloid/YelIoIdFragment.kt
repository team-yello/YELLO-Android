package com.el.yello.presentation.onboarding.fragment.yelloid

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloIdBinding
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.AmplitudeManager
import com.el.yello.util.extension.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.extension.colorOf
import com.example.ui.state.UiState
import com.example.ui.extension.setOnSingleClickListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class YelIoIdFragment : BindingFragment<FragmentYelloIdBinding>(R.layout.fragment_yello_id) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()
    private var searchJob: Job? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        setDeleteBtnClickListener()
        setConfirmBtnClickListener()
        observeGetValidYelloIdState()
        observeEditTextForValidId()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnYelloIdNext.setOnSingleClickListener {
            findNavController().navigate(R.id.action_yelIoIdFragment_to_addFriendFragment)
            val activity = requireActivity() as OnBoardingActivity
            activity.progressBarPlus()
            amplitudeYelloIdInfo()
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnIdDelete.setOnClickListener {
            binding.etId.text.clear()
        }
    }

    private fun observeEditTextForValidId() {
        binding.etId.doAfterTextChanged { input ->
            searchJob?.cancel()
            searchJob = viewModel.viewModelScope.launch {
                delay(debounceTime)
                input?.toString()?.let {
                    viewModel.getValidYelloId(it)
                    binding.btnYelloIdNext.isEnabled = true
                }
            }
        }
    }

    private fun observeGetValidYelloIdState() {
        viewModel.getValidYelloIdState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data) {
                        if (viewModel.idText.value.toString().isNotBlank()) {
                            initIdEditTextViewError()
                        }
                        return@observe
                    }
                    viewModel.resetGetValidYelloId()
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
                }
            }
        }
    }

    private fun initIdEditTextViewError() {
        with(binding) {
            etId.setBackgroundResource(R.drawable.shape_fill_red20_line_semantic_status_red500_rect_8)
            btnIdDelete.setBackgroundResource(R.drawable.ic_onboarding_delete_red)
            tvIdErrorFirst.text = getString(R.string.onboarding_name_id_duplicate_id_msg)
            tvIdErrorFirst.setTextColor(colorOf(R.color.semantic_red_500))
            tvIdErrorSecond.visibility = View.INVISIBLE
            tvIdErrorThird.visibility = View.INVISIBLE
            btnYelloIdNext.setBackgroundResource(R.drawable.shape_grayscales_800_fill_100_rect)
            btnYelloIdNext.setTextColor(colorOf(R.color.grayscales_700))
            btnYelloIdNext.isEnabled = false
        }
    }

    private fun amplitudeYelloIdInfo() {
        AmplitudeManager.trackEventWithProperties(
            EVENT_CLICK_ONBOARDING_NEXT,
            JSONObject().put(NAME_ONBOARD_VIEW, VALUE_ID),
        )
        AmplitudeManager.updateUserProperties(PROPERTY_USER_ID, viewModel.id)
    }

    companion object {
        private const val EVENT_CLICK_ONBOARDING_NEXT = "click_onboarding_next"
        private const val NAME_ONBOARD_VIEW = "onboard_view"
        private const val VALUE_ID = "id"
        private const val PROPERTY_USER_ID = "user_id"
        private const val debounceTime = 100L
    }
}
