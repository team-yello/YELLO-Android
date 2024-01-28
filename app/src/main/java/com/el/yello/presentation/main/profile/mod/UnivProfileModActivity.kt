package com.el.yello.presentation.main.profile.mod

import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileUnivModBinding
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnivProfileModActivity :
    BindingActivity<ActivityProfileUnivModBinding>(R.layout.activity_profile_univ_mod) {

    private val viewModel by viewModels<UnivProfileModViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUserDetail()

    }

    private fun setUserDetail() {
        binding.vm = viewModel
    }
}