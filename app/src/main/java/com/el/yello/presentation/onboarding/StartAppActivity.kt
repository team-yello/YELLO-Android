package com.el.yello.presentation.onboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.el.yello.R
import com.el.yello.databinding.ActivityStartAppBinding
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class StartAppActivity :
    BindingActivity<ActivityStartAppBinding>(R.layout.activity_start_app) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        initTutorialView()
    }

    private fun initTutorialView() {
        binding.btnStartYello.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_onboarding_notification")
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startTutorialActivity()
            } else {
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS,
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}
