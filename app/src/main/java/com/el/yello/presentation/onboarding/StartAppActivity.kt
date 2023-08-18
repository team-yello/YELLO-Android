package com.el.yello.presentation.onboarding

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.el.yello.R
import com.el.yello.databinding.ActivityStartAppBinding
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener

class StartAppActivity :
    BindingActivity<ActivityStartAppBinding>(R.layout.activity_start_app) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTutorialView()

    }

    private fun startTutorialActivity() {
        val intent = TutorialAActivity.newIntent(this, false).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        finish()
    }

    private fun initTutorialView() {
        binding.btnStartYello.setOnSingleClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.POST_NOTIFICATIONS,
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        PERMISSION_REQUEST_CODE,
                    )
                } else {
                    startTutorialActivity()
                }
            } else {
                startTutorialActivity()
            }
            startTutorialActivity()
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
