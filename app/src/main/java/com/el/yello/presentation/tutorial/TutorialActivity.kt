package com.el.yello.presentation.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity.Companion.EXTRA_CODE_TEXT_EMPTY
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.extension.boolExtra
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialActivity : ComponentActivity() {

    private val isFromOnBoarding by boolExtra()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val isCodeTextEmpty = intent.getBooleanExtra(EXTRA_CODE_TEXT_EMPTY, false)
        
        setContent {
            YelloTheme {
                TutorialRoute(
                    isCodeTextEmpty = isCodeTextEmpty,
                    isFromOnBoarding = isFromOnBoarding,
                    navigateToTutorialEnd = {
                        startActivity(Intent(this@TutorialActivity, TutorialEndActivity::class.java))
                        finish()
                    },
                    navigateToTutorialEndPlus = {
                        startActivity(Intent(this@TutorialActivity, TutorialEndPlusActivity::class.java))
                        finish()
                    },
                    navigateToMainActivity = {
                        val intent = Intent(this@TutorialActivity, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context, isFromOnBoarding: Boolean) =
            Intent(context, TutorialActivity::class.java).apply {
                putExtra(EXTRA_FROM_ONBOARDING, isFromOnBoarding)
            }

        const val EXTRA_FROM_ONBOARDING = "isFromOnBoarding"
    }
}
