package com.el.yello.presentation.onboarding.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityNameEditBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNameActivity :
    BindingActivity<ActivityNameEditBinding>(R.layout.activity_name_edit) {

    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        setConfirmBtnClickListener()
        setDeleteBtnClickListener()
    }

    private fun setConfirmBtnClickListener() {
        getIntentExtraData()
        binding.btnNameNext.setOnSingleClickListener {
            val intent = Intent(this, OnBoardingActivity::class.java).apply {
                intent.putExtra("EXTRA_KAKAO_ID", viewModel.kakaoId.toLong())
                intent.putExtra("EXTRA_NAME", viewModel.name)
                intent.putExtra("EXTRA_GENDER", viewModel.gender)
                intent.putExtra("EXTRA_EMAIL", viewModel.email)
                intent.putExtra("EXTRA_PROFILE_IMAGE", viewModel.profileImg)
            }
            startActivity(intent)
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnNameDelete.setOnSingleClickListener {
            binding.etName.text.clear()
        }
    }

    private fun getIntentExtraData() {
        val bundle: Bundle? = intent.extras
        intent.apply {
            if (bundle != null) {
                viewModel.kakaoId = bundle.getLong("EXTRA_KAKAO_ID", 0).toString()
                viewModel.name = bundle.getString("EXTRA_NAME", "")
                viewModel.gender = bundle.getString("EXTRA_GENDER", "")
                viewModel.email = bundle.getString("EXTRA_EMAIL", "")
                viewModel.profileImg = bundle.getString("EXTRA_PROFILE_IMAGE", "")
            }
        }
    }
}
