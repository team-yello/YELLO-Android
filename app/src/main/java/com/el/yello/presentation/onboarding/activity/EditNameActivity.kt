package com.el.yello.presentation.onboarding.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityNameEditBinding
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class EditNameActivity : BindingFragment<ActivityNameEditBinding>(R.layout.activity_name_edit) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setConfirmBtnClickListener()
        setDeleteBtnClickListener()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnNameNext.setOnSingleClickListener {
            // TODO : onboardingactivity 이동
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnNameDelete.setOnSingleClickListener {
            binding.etName.text.clear()
        }
    }
}
