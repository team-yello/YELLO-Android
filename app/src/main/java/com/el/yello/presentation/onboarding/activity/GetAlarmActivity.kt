package com.el.yello.presentation.onboarding.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.el.yello.R
import com.el.yello.databinding.ActivityGetAlarmBinding
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.intent.boolExtra
import com.example.ui.view.setOnSingleClickListener

class GetAlarmActivity :
    BindingActivity<ActivityGetAlarmBinding>(R.layout.activity_get_alarm) {

    private val isFromOnBoarding by boolExtra()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                AmplitudeUtils.updateUserProperties("user_pushnotification", "enabled")
                startTutorialActivity()
            } else {
                AmplitudeUtils.updateUserProperties("user_pushnotification", "disabled")
                startTutorialActivity()
            }
        }

    private fun startTutorialActivity() {
        val isCodeTextEmpty =
            intent.getBooleanExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, false)
        val intent = TutorialAActivity.newIntent(this, false).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, isCodeTextEmpty)
            putExtra(TutorialAActivity.EXTRA_FROM_ONBOARDING, isFromOnBoarding)
        }
        startActivity(intent)
        finish()
    }

    private fun askNotificationPermission() {
        binding.btnStartYello.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_onboarding_notification")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS,
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    startTutorialActivity()
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                startTutorialActivity()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(NONE_ANIMATION, NONE_ANIMATION)
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context, isFromOnBoarding: Boolean) =
            Intent(context, GetAlarmActivity::class.java).apply {
                putExtra("isFromOnBoarding", isFromOnBoarding)
            }
        private const val NONE_ANIMATION = 0
    }
}
