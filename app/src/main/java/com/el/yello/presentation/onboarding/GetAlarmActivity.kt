package com.el.yello.presentation.onboarding

import android.Manifest
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
import com.example.ui.view.setOnSingleClickListener

class GetAlarmActivity :
    BindingActivity<ActivityGetAlarmBinding>(R.layout.activity_get_alarm) {

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
        val intent = TutorialAActivity.newIntent(this, false).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
}
