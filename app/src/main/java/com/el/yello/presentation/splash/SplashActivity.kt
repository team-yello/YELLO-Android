package com.el.yello.presentation.splash

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.el.yello.BuildConfig
import com.el.yello.R
import com.el.yello.databinding.ActivitySplashBinding
import com.el.yello.presentation.auth.SignInActivity
import com.el.yello.presentation.main.MainActivity
import com.el.yello.util.NetworkManager
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel by viewModels<SplashViewModel>()

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showExtraToastMsg()
        initAppUpdate()
    }

    private fun showExtraToastMsg() {
        yelloSnackbar(binding.root, intent.getStringExtra(EXTRA_TOAST_MSG) ?: return)
    }

    private fun initAppUpdate() {
        if (NetworkManager.checkNetworkState(this)) {
            if (BuildConfig.DEBUG) {
                initSplash()
            } else {
                val appUpdateInfoTask = appUpdateManager.appUpdateInfo
                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                        appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
                        requestUpdate(appUpdateInfo)
                    } else {
                        initSplash()
                    }
                }.addOnFailureListener {
                    initSplash()
                }
            }
        } else {
            AlertDialog.Builder(this)
                .setTitle("안내")
                .setMessage("인터넷 연결을 확인해주세요.")
                .setCancelable(false)
                .setPositiveButton(
                    "확인",
                    DialogInterface.OnClickListener { dialog, _ ->
                        finishAffinity()
                    },
                )
                .create()
                .show()
        }
    }

    private fun initSplash() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.getIsAutoLogin()) {
                navigateToMainScreen()
            } else {
                navigateToSignInScreen()
            }
        }, 3000)
    }

    private fun requestUpdate(appUpdateInfo: AppUpdateInfo) {
        runCatching {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                activityResultLauncher,
                AppUpdateOptions.newBuilder(IMMEDIATE)
                    .setAllowAssetPackDeletion(true)
                    .build(),
            )
        }.onFailure {
            Timber.e(it)
        }
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

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode != RESULT_OK) {
                toast(getString(R.string.splash_update_error))
                finishAffinity()
            }
        }

    override fun onResume() {
        super.onResume()

        if (!BuildConfig.DEBUG) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    runCatching {
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            activityResultLauncher,
                            AppUpdateOptions.newBuilder(IMMEDIATE)
                                .build(),
                        )
                    }.onFailure {
                        Timber.e(it)
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_TOAST_MSG = "TOAST_MSG"
    }
}
