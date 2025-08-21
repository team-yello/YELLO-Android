package com.el.yello.presentation.getalarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.el.yello.presentation.getalarm.screen.GetAlarmRoute
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.tutorial.TutorialActivity
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.extension.boolExtra
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetAlarmActivity : ComponentActivity() {

    private val isFromOnBoarding by boolExtra()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isCodeTextEmpty =
            intent.getBooleanExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, false)

        setContent {
            YelloTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GetAlarmRoute(
                        isFromOnBoarding = isFromOnBoarding,
                        isCodeTextEmpty = isCodeTextEmpty,
                        navigateToTutorial = ::startTutorialActivity
                    )
                }
            }
        }
    }

    private fun startTutorialActivity() {
        val isCodeTextEmpty =
            intent.getBooleanExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, false)

        val intent = TutorialActivity.newIntent(this, false).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(OnBoardingActivity.EXTRA_CODE_TEXT_EMPTY, isCodeTextEmpty)
            putExtra(TutorialActivity.EXTRA_FROM_ONBOARDING, isFromOnBoarding)
        }
        startActivity(intent)
        finish()
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context, isFromOnBoarding: Boolean) =
            Intent(context, GetAlarmActivity::class.java).apply {
                putExtra("isFromOnBoarding", isFromOnBoarding)
            }
    }
}
