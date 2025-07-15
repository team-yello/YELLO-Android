package com.el.yello.presentation.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.compose.theme.YelloTheme
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ProfileQuitOneActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            YelloTheme {
                ProfileQuitOneRoute(
                    navigateBack = { finish() },
                    navigateToProfileQuitTwo = {
                        AmplitudeManager.trackEventWithProperties(
                            EVENT_CLICK_PROFILE_WITHDRAWAL,
                            JSONObject().put(NAME_WITHDRAWAL_BUTTON, VALUE_WITHDRAWAL_TWO),
                        )
                        Intent(this, ProfileQuitTwoActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(this)
                        }
                    }
                )
            }
        }
    }

    companion object {
        private const val EVENT_CLICK_PROFILE_WITHDRAWAL = "click_profile_withdrawal"
        private const val NAME_WITHDRAWAL_BUTTON = "withdrawal_button"
        private const val VALUE_WITHDRAWAL_TWO = "withdrawal2"
    }
}
