package com.el.yello.presentation.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.BuildConfig
import com.el.yello.R
import com.el.yello.databinding.ActivitySettingBinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.restart.restartApp
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class SettingActivity :
    BindingActivity<ActivitySettingBinding>(R.layout.activity_setting) {

    private val viewModel by viewModels<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtnListener()
        initQuitBtnListener()
        initCenterBtnListener()
        initPrivacyBtnListener()
        initServiceBtnListener()
        initLogoutBtnListener()
        setVersionCode()
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
            AmplitudeUtils.trackEventWithProperties("click_profile_logout")
            viewModel.logoutKakaoAccount()
        }
    }

    private fun initBackBtnListener() {
        binding.btnProfileManageBack.setOnSingleClickListener { finish() }
    }

    private fun initQuitBtnListener() {
        binding.btnProfileManageQuit.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_profile_withdrawal",
                JSONObject().put("withdrawal_button", "withdrawal1"),
            )
            Intent(this, ProfileQuitOneActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
            finish()
        }
    }

    private fun setVersionCode() {
        binding.tvProfileManageVersion.text =
            getString(R.string.profile_manage_tv_version, BuildConfig.VERSION_NAME)
    }

    private fun observeKakaoLogoutState() {
        viewModel.kakaoLogoutState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    AmplitudeUtils.trackEventWithProperties("complete_profile_logout")
                    lifecycleScope.launch {
                        delay(500)
                        restartApp(binding.root.context, null)
                    }
                }

                is UiState.Failure -> yelloSnackbar(
                    binding.root,
                    getString(R.string.internet_connection_error_msg)
                )

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    companion object {
        const val CUSTOMER_CENTER_URL = "http://pf.kakao.com/_pcFzG/chat"
        const val PRIVACY_URL = "https://yell0.notion.site/97f57eaed6c749bbb134c7e8dc81ab3f"
        const val SERVICE_URL = "https://yell0.notion.site/2afc2a1e60774dfdb47c4d459f01b1d9"
    }
}
