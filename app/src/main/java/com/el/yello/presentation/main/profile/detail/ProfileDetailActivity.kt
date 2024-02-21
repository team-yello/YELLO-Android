package com.el.yello.presentation.main.profile.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileDetailBinding
import com.el.yello.presentation.main.profile.info.ProfileFragment.Companion.TYPE_UNIVERSITY
import com.el.yello.presentation.main.profile.mod.SchoolProfileModActivity
import com.el.yello.presentation.main.profile.mod.UnivProfileModActivity
import com.el.yello.util.Utils.setImageOrBasicThumbnail
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.state.UiState
import com.example.ui.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileDetailActivity :
    BindingActivity<ActivityProfileDetailBinding>(R.layout.activity_profile_detail) {

    private val viewModel by viewModels<ProfileDetailViewModel>()

    private var isUnivProfile = false

    private val profileModifyResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> yelloSnackbar(
                    binding.root,
                    getString(R.string.profile_mod_success)
                )

                RESULT_PROFILE_MODIFY_FAILURE -> yelloSnackbar(
                    binding.root,
                    getString(R.string.internet_connection_error_msg)
                )
            }
        }

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
        binding.layoutProfileDetailSchool.setOnSingleClickListener { navigateToModActivity() }
        binding.layoutProfileModSchool.setOnSingleClickListener { navigateToModActivity() }
        binding.layoutProfileModUniv.setOnSingleClickListener { navigateToModActivity() }
    }

    private fun navigateToModActivity() {
        if (isUnivProfile) {
            profileModifyResultLauncher.launch(Intent(this, UnivProfileModActivity::class.java))
        } else {
            profileModifyResultLauncher.launch(Intent(this, SchoolProfileModActivity::class.java))
        }
        viewModel.resetViewModelState()
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
                    binding.ivProfileDetailThumbnail.setImageOrBasicThumbnail(state.data.profileImageUrl)
                    isUnivProfile = state.data.groupType == TYPE_UNIVERSITY
                    binding.layoutProfileModUniv.isVisible = isUnivProfile
                    binding.layoutProfileModSchool.isVisible = !isUnivProfile
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
                }

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeKakaoDataResult() {
        viewModel.getKakaoInfoResult.flowWithLifecycle(lifecycle).onEach { result ->
            when (result) {
                is ImageChangeState.Success -> {
                    binding.ivProfileDetailThumbnailEmpty.isVisible = true
                }

                is ImageChangeState.NotChanged -> {
                    yelloSnackbar(binding.root, getString(R.string.profile_mod_already_changed))
                }

                is ImageChangeState.Error -> {
                    yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeModProfileState() {
        viewModel.postToModProfileState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.ivProfileDetailThumbnail.setImageOrBasicThumbnail(state.data)
                    yelloSnackbar(binding.root, getString(R.string.profile_detail_image_change))
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
                }

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
            delay(500)
            binding.ivProfileDetailThumbnailEmpty.isVisible = false
        }.launchIn(lifecycleScope)
    }

    companion object {
        const val RESULT_PROFILE_MODIFY_FAILURE = 2
    }
}