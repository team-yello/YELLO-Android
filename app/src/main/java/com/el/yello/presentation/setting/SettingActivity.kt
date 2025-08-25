package com.el.yello.presentation.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.util.Utils.restartApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YelloTheme {
                SettingRoute(
                    navigateBack = { finish() },

                    navigateCustomerSupport = {

                        startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(CUSTOMER_CENTER_URL))
                        )
                    },
                    navigatePrivacyPolicy = {
                        startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL)),
                        )
                    },
                    navigateTermsOfService = {
                        startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(SERVICE_URL)),
                        )
                    },
                    navigateAccountDeletion = {
                        AmplitudeManager.trackEventWithProperties(
                            EVENT_CLICK_PROFILE_WITHDRAWAL,
                            JSONObject().put(
                                NAME_WITHDRAWAL_BUTTON,
                                VALUE_WITHDRAWAL_ONE
                            ),
                        )
                        Intent(this, ProfileQuitOneActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(this)
                        }
                        finish()
                    },
                    successLogout = {
                        AmplitudeManager.trackEventWithProperties(COMPLETE_PROFILE_LOGOUT)
                        lifecycleScope.launch {
                            delay(500)
                            restartApp(this@SettingActivity, null)
                        }
                    }
                )
            }
        }
    }

    companion object {
        const val CUSTOMER_CENTER_URL = "http://pf.kakao.com/_pcFzG/chat"
        const val PRIVACY_URL = "https://yell0.notion.site/97f57eaed6c749bbb134c7e8dc81ab3f"
        const val SERVICE_URL = "https://yell0.notion.site/2afc2a1e60774dfdb47c4d459f01b1d9"

        const val EVENT_CLICK_PROFILE_WITHDRAWAL = "click_profile_withdrawal"
        const val NAME_WITHDRAWAL_BUTTON = "withdrawal_button"
        const val VALUE_WITHDRAWAL_ONE = "withdrawal1"
        const val CLICK_PROFILE_LOGOUT = "click_profile_logout"
        const val COMPLETE_PROFILE_LOGOUT = "complete_profile_logout"
    }
}

