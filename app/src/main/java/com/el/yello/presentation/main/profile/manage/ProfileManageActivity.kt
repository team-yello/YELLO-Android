package com.el.yello.presentation.main.profile.manage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileManageBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.user.UserApiClient
import com.el.yello.presentation.main.profile.ProfileViewModel
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

        initBackButton(this)
        initProfileQuitActivityWithoutFinish()
        initCenterButton()
        initPrivacyButton()
        initServiceButton()
        initLogoutButton()
    }

    private fun initCenterButton() {
        binding.btnProfileManageCenter.setOnSingleClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://yell0.notion.site/YELLO-34028220a873416b91d5d2f1cd827432?pvs=4"),
                ),
            )
        }
    }

    private fun initPrivacyButton() {
        binding.btnProfileManagePrivacy.setOnSingleClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://yell0.notion.site/97f57eaed6c749bbb134c7e8dc81ab3f"),
                ),
            )
        }
    }

    private fun initServiceButton() {
        binding.btnProfileManageService.setOnSingleClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://yell0.notion.site/2afc2a1e60774dfdb47c4d459f01b1d9"),
                ),
            )
        }
    }

    private fun initLogoutButton() {
        binding.btnProfileManageLogout.setOnSingleClickListener {
            logoutKakaoAccount()
        }
    }

    private fun initBackButton(activity: Activity) {
        binding.btnProfileManageBack.setOnSingleClickListener {
            activity.finish()
        }
    }

    private fun initProfileQuitActivityWithoutFinish() {
        binding.btnProfileManageQuit.setOnSingleClickListener {
            Intent(this, ProfileQuitActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }

    private fun logoutKakaoAccount() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Timber.d(getString(R.string.profile_error_logout) + ": $error")
            } else {
                lifecycleScope.launch {
                    viewModel.clearLocalInfo()
                    delay(500)
                    restartApp(this@ProfileManageActivity)
                }
            }
        }
    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}
