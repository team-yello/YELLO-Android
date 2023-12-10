package com.el.yello.presentation.onboarding.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityNameEditBinding
import com.el.yello.presentation.main.profile.manage.ProfileQuitDialog
import com.el.yello.presentation.main.profile.manage.ProfileQuitTwoActivity
import com.el.yello.presentation.onboarding.fragment.EditName.EditNameDialog
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNameActivity :
    BindingActivity<ActivityNameEditBinding>(R.layout.activity_name_edit) {

    private val viewModel by viewModels<OnBoardingViewModel>()

    private var EditNameDialog: EditNameDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        setConfirmBtnClickListener()
        setDeleteBtnClickListener()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnNameNext.setOnSingleClickListener {
            //TODO : onboardingactivity 이동
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnNameDelete.setOnSingleClickListener {
            binding.etName.text.clear()
        }
    }

    private companion object {
        const val QUIT_DIALOG = "quitDialog"
    }
}
