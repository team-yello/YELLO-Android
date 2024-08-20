package com.el.yello.presentation.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.BuildConfig.DEBUG
import com.el.yello.R
import com.el.yello.databinding.ActivitySplashBinding
import com.el.yello.presentation.auth.SignInActivity
import com.el.yello.presentation.main.MainActivity
import com.el.yello.util.extension.yelloSnackbar
import com.el.yello.util.manager.NetworkManager
import com.example.ui.base.BindingActivity
import com.example.ui.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initView()
        initObserver()
    }

    private fun initView() {
        showExtraToastMsg()
        checkNetworkUpdateState()
    }

    private fun showExtraToastMsg() {
        yelloSnackbar(binding.root, intent.getStringExtra(EXTRA_TOAST_MSG) ?: return)
    }

    private fun checkNetworkUpdateState() {
        if (NetworkManager.checkNetworkState(this)) {
            if (DEBUG) {
                initSplashView()
            } else {
                viewModel.checkLatestUpdate()
            }
        } else {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.splash_guide))
                .setMessage(getString(R.string.splash_network_description))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.splash_confirm)) { _, _ ->
                    finishAffinity()
                }
                .create()
                .show()
        }
    }

    private fun initObserver() {
        observeIsLatestVersion()
    }

    private fun observeIsLatestVersion() {
        viewModel.isLatestVersion.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Empty, is UiState.Loading -> {
                        return@onEach
                    }

                    is UiState.Success -> {
                        val isLatestVersion = state.data

                        if (isLatestVersion) {
                            initSplashView()
                        } else {
                            showInAppUpdateDialog()
                        }
                    }

                    is UiState.Failure -> {
                        // TODO : 인앱 업데이트 필요 여부 조회 실패 시 UI 처리
                        Timber.e(state.msg)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun initSplashView() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.getIsAutoLogin()) {
                navigateToMainScreen()
            } else {
                navigateToSignInScreen()
            }
        }, 3000)
    }

    private fun navigateToMainScreen() {
        var type: String? = ""
        var path: String? = ""
        if (intent.extras != null) {
            type = intent.getStringExtra("type")
            path = intent.getStringExtra("path")
        }

        MainActivity.getIntent(this, type, path).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    private fun navigateToSignInScreen() {
        Intent(this, SignInActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    private fun showInAppUpdateDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.splash_guide))
            .setMessage(getString(R.string.slash_update_description))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.splash_confirm)) { _, _ ->
                navigateToMarket()
            }
            .create()
            .show()
    }

    private fun navigateToMarket() {
        val uri = Uri.parse(URI_MARKET + packageName)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onResume() {
        super.onResume()

        if (DEBUG) {
            initSplashView()
        } else {
            viewModel.checkLatestUpdate()
        }
    }

    companion object {
        private const val EXTRA_TOAST_MSG = "TOAST_MSG"
        private const val URI_MARKET = "market://details?id="
    }
}
