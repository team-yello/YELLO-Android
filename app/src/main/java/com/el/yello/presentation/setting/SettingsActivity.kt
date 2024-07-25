package com.el.yello.presentation.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.util.extension.yelloSnackbar
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.extension.toast
import com.example.ui.state.UiState
import com.example.ui.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    private val viewModel by viewModels<SettingViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YelloTheme {
                SettingScreen(
                    viewModel = viewModel,
                    onClickBack = { finish() },
                    onCustomerSupportClick = {

                        startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(CUSTOMER_CENTER_URL))
                        )
                    },
                    onClickPrivacyPolicyClick = {
                        startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL)),
                        )
                    },
                    onClickTermsOfServiceClick = {
                        startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(SERVICE_URL)),
                        )
                    },
                    onAccountDeletionClick = {
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

