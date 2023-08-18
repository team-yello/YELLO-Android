package com.el.yello.presentation.onboarding.fragment.startapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.el.yello.R
import com.el.yello.databinding.FragmentStartAppBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class StartAppFragment : BindingFragment<FragmentStartAppBinding>(R.layout.fragment_start_app) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? OnBoardingActivity)?.hideViews()
        initTutorialView()
    }

    private fun startTutorialActivity() {
        val intent = TutorialAActivity.newIntent(requireContext(), true).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private fun initTutorialView() {
        binding.btnStartYello.setOnSingleClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.POST_NOTIFICATIONS,
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        PERMISSION_REQUEST_CODE,
                    )
                } else {
                    startTutorialActivity()
                }
            } else {
                startTutorialActivity()
            }
            (activity as? OnBoardingActivity)?.endTutorialActivity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startTutorialActivity()
            } else {
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
