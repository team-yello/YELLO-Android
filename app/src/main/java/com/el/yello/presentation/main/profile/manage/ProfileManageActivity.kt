package com.el.yello.presentation.main.profile.manage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileManageBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ProfileManageActivity :
    BindingActivity<ActivityProfileManageBinding>(R.layout.activity_profile_manage) {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtnListener()
        initQuitBtnListener()
        initCenterBtnListener()
        initPrivacyBtnListener()
        initServiceBtnListener()
        initLogoutBtnListener()
        observeKakaoLogoutState()
    }

    private fun initCenterBtnListener() {
        binding.btnProfileManageCenter.setOnSingleClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(CUSTOMER_CENTER_URL)),
            )
        }
    }

    private fun initPrivacyBtnListener() {
        binding.btnProfileManagePrivacy.setOnSingleClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL)),
            )
        }
    }

    private fun initServiceBtnListener() {
        binding.btnProfileManageService.setOnSingleClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(SERVICE_URL)),
            )
        }
    }

    private fun initLogoutBtnListener() {
        binding.btnProfileManageLogout.setOnSingleClickListener {
            viewModel.logoutKakaoAccount()
        }
    }

    private fun initBackBtnListener() {
        binding.btnProfileManageBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initQuitBtnListener() {
        binding.btnProfileManageQuit.setOnSingleClickListener {
            Intent(this, ProfileQuitActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }

    private fun observeKakaoLogoutState() {
        viewModel.kakaoLogoutState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    lifecycleScope.launch {
                        delay(500)
                        restartApp()
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                    Timber.d(getString(R.string.profile_error_logout, state.msg))
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    private fun restartApp() {
        val componentName = packageManager.getLaunchIntentForPackage(packageName)?.component
        Intent.makeRestartActivityTask(componentName).apply {
            startActivity(this)
        }
        Runtime.getRuntime().exit(0)
    }

    private companion object {
        const val CUSTOMER_CENTER_URL =
            "https://yell0.notion.site/YELLO-34028220a873416b91d5d2f1cd827432?pvs=4"
        const val PRIVACY_URL =
            "https://yell0.notion.site/97f57eaed6c749bbb134c7e8dc81ab3f"
        const val SERVICE_URL =
            "https://yell0.notion.site/2afc2a1e60774dfdb47c4d459f01b1d9"
    }
}
