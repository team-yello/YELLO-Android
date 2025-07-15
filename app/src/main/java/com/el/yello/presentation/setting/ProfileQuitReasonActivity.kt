package com.el.yello.presentation.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.el.yello.R
import com.el.yello.presentation.main.MainActivity
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.extension.toast
import com.example.ui.util.Utils.restartApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileQuitReasonActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YelloTheme {
                ProfileReasonRoute(
                    onBackClick = { finish() },
                    navigateToMainActivity = {
                        AmplitudeManager.trackEventWithProperties(EVENT_CLICK_WITHDRAWAL_RECOMMEND)
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra(RECOMMEND_FRAGMENT, true)
                        }
                        startActivity(intent)
                    },
                    navigateToReStart = {
                        AmplitudeManager.trackEventWithProperties(EVENT_COMPLETE_WITHDRAWAL)
                        restartApp(this, null)
                    },
                    showToast = {
                        toast(getString(R.string.internet_connection_error_msg))
                    }
                )
            }
        }
    }

    private companion object {
        private const val RECOMMEND_FRAGMENT = "RecommendFragment"
        private const val EVENT_CLICK_WITHDRAWAL_RECOMMEND = "click_withdrawal_recommend"
        private const val EVENT_COMPLETE_WITHDRAWAL = "complete_withdrawal"
    }
}
