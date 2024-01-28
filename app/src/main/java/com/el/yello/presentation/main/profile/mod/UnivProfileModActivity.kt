package com.el.yello.presentation.main.profile.mod

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileUnivModBinding
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UnivProfileModActivity :
    BindingActivity<ActivityProfileUnivModBinding>(R.layout.activity_profile_univ_mod) {

    private val viewModel by viewModels<UnivProfileModViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUserDetail()
        observeGetUserDataResult()
    }

    private fun setUserDetail() {
        binding.vm = viewModel
        viewModel.getUserDataFromServer()
    }

    private fun observeGetUserDataResult() {
        viewModel.getUserDataResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) yelloSnackbar(binding.root, getString(R.string.msg_error))
        }.launchIn(lifecycleScope)
    }
}