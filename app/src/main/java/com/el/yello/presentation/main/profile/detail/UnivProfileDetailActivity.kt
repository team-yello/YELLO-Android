package com.el.yello.presentation.main.profile.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileUnivDetailBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.main.profile.mod.UnivProfileModActivity
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.activity.navigateTo
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
        observeKakaoDataState()
    }

    private fun setUserDetail() {
        binding.vm = viewModel
        viewModel.getUserDataFromServer()
    }

    private fun initProfileModBtnListener() {
        binding.btnModSchool.setOnSingleClickListener { this.navigateTo<UnivProfileModActivity>() }
        binding.btnModSubgroup.setOnSingleClickListener { this.navigateTo<UnivProfileModActivity>()  }
        binding.btnModYear.setOnSingleClickListener { this.navigateTo<UnivProfileModActivity>()  }
    }

    private fun initChangeThumbnailBtnListener() {
        binding.btnChangeKakaoImage.setOnSingleClickListener {
            viewModel.getUserInfoFromKakao()
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

    private fun observeKakaoDataState() {
        viewModel.getKakaoDataState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.ivProfileDetailThumbnail.setImageOrBasicThumbnail(state.data)
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.sign_in_error_connection))
                }

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }
}