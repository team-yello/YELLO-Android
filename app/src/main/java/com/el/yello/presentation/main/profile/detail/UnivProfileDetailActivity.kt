package com.el.yello.presentation.main.profile.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileUnivDetailBinding
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UnivProfileDetailActivity :
    BindingActivity<ActivityProfileUnivDetailBinding>(R.layout.activity_profile_univ_detail) {

    private val viewModel by viewModels<UnivProfileDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUserDetail()
        initProfileModBtnListener()
        initChangeThumbnailBtnListener()
        observeUserDataState()
    }

    private fun setUserDetail() {
        binding.vm = viewModel
        viewModel.getUserDataFromServer()
    }

    private fun initProfileModBtnListener() {
        // TODO: 이동
    }

    private fun initChangeThumbnailBtnListener() {
        // TODO: 카카오
        binding.btnChangeKakaoImage.setOnSingleClickListener {

        }
    }

    private fun observeUserDataState() {
        viewModel.getUserDataState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.ivProfileDetailThumbnail.setImageOrBasicThumbnail(state.data)
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.profile_error_user_data))
                }

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }
}