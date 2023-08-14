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
import com.el.yello.presentation.main.MainActivity
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class StartAppFragment : BindingFragment<FragmentStartAppBinding>(R.layout.fragment_start_app) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    startMainActivity()
                }
            } else {
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMainActivity()
            } else {
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
