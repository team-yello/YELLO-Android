package com.el.yello.presentation.onboarding.fragment.nameid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentNameBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class NameFragment : BindingFragment<FragmentNameBinding>(R.layout.fragment_name) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        setDeleteBtnClickListener()
        setConfirmBtnClickListener()
    }
    private fun setConfirmBtnClickListener() {
        binding.btnNameNext.setOnSingleClickListener {
            // findNavController().navigate(R.id.action_nameFragment_to_yelIoIdFragment)
            val activity = requireActivity() as OnBoardingActivity
            activity.progressBarPlus()
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnNameDelete.setOnSingleClickListener {
            binding.etName.text.clear()
        }
    }
}
