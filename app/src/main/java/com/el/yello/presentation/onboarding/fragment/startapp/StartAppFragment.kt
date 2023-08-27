package com.el.yello.presentation.onboarding.fragment.startapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.el.yello.R
import com.el.yello.databinding.FragmentStartAppBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class StartAppFragment : BindingFragment<FragmentStartAppBinding>(R.layout.fragment_start_app) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartYello.setOnSingleClickListener {
            askNotificationPermission()
        }
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

    private fun askNotificationPermission() {
        AmplitudeUtils.trackEventWithProperties("click_onboarding_notification")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startTutorialActivity()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun startTutorialActivity() {
        val intent = TutorialAActivity.newIntent(requireContext(), true).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        (activity as? OnBoardingActivity)?.hideViews()
        (activity as? OnBoardingActivity)?.endTutorialActivity()
        requireActivity().finish()
    }
}
