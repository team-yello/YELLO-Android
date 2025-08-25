package com.el.yello.presentation.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.compose.theme.YelloTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileQuitTwoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YelloTheme {
                ProfileQuitTwoRoute(
                    navigateBack = { finish() },
                    navigateToProfileQuitReason = {
                        Intent(this, ProfileQuitReasonActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(this)
                        }
                    }
                )
            }
        }
    }
}
