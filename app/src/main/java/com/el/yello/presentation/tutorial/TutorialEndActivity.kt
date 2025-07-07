package com.el.yello.presentation.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.tutorial.screen.TutorialEndRoute
import com.example.ui.compose.theme.YelloTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialEndActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isPlus = intent.getBooleanExtra(EXTRA_IS_PLUS, false)
        setContent {
            YelloTheme {
                TutorialEndRoute(
                    isPlus = isPlus,
                    navigateToMainActivity = {
                        val intent = Intent(this@TutorialEndActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context, isPlus: Boolean) =
            Intent(context, TutorialEndActivity::class.java).apply {
                putExtra(EXTRA_IS_PLUS, isPlus)
            }
        private const val EXTRA_IS_PLUS = "IS_PLUS"
    }
}
