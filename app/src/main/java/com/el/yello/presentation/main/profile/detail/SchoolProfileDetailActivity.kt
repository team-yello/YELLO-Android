package com.el.yello.presentation.main.profile.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileSchoolDetailBinding
import com.el.yello.presentation.main.profile.mod.SchoolProfileModActivity
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.activity.navigateTo
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SchoolProfileDetailActivity :
    BindingActivity<ActivityProfileSchoolDetailBinding>(R.layout.activity_profile_school_detail) {

    private val viewModel by viewModels<SchoolProfileDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        initProfileModBtnListener()
        initChangeThumbnailBtnListener()
        initBackBtnListener()
        observeUserDataState()
        observeKakaoDataResult()
        observeModProfileState()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getUserDataFromServer()
    }

    private fun initProfileModBtnListener() {
        binding.btnModSchool.setOnSingleClickListener {
            this.navigateTo<SchoolProfileModActivity>()
            viewModel.resetViewModelState()
        }
        binding.btnModGrade.setOnSingleClickListener {
            this.navigateTo<SchoolProfileModActivity>()
            viewModel.resetViewModelState()
        }
        binding.btnModClassroom.setOnSingleClickListener {
            this.navigateTo<SchoolProfileModActivity>()
            viewModel.resetViewModelState()
        }
    }

    private fun initChangeThumbnailBtnListener() {
        binding.btnChangeKakaoImage.setOnSingleClickListener {
            viewModel.getUserInfoFromKakao()
        }
    }

    private fun initBackBtnListener() {
        binding.btnProfileDetailBack.setOnSingleClickListener { finish() }
    }

    private fun observeUserDataState() {
        viewModel.getUserDataState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.ivProfileDetailThumbnail.setImageOrBasicThumbnail(state.data)
                }

                is UiState.Failure -> {
                    toast(getString(R.string.profile_error_user_data))
                }

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeKakaoDataResult() {
        viewModel.getKakaoInfoResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) yelloSnackbar(binding.root, getString(R.string.msg_error))
        }.launchIn(lifecycleScope)
    }

    private fun observeModProfileState() {
        viewModel.postToModProfileState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.ivProfileDetailThumbnail.setImageOrBasicThumbnail(state.data)
                    toast(getString(R.string.profile_detail_image_change))
                }

                is UiState.Failure -> {
                    toast(getString(R.string.sign_in_error_connection))
                }

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }
}