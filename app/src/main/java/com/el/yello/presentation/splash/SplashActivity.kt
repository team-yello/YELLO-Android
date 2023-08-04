package com.el.yello.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.el.yello.R
import com.el.yello.presentation.auth.SignInActivity
import com.el.yello.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<SplashViewModel>()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.getIsAutoLogin()) {
                navigateToMainScreen()
            } else {
                navigateToSignInScreen()
            }
        }, 3000)
    }

    private fun navigateToMainScreen() {
        Intent(this@SplashActivity, MainActivity::class.java).apply {
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
}
