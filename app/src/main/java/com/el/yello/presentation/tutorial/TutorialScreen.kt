package com.el.yello.presentation.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.YelloTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun TutorialRoute(
    viewModel: TutorialViewModel = hiltViewModel(),
    isCodeTextEmpty: Boolean,
    isFromOnBoarding: Boolean,
    navigateToTutorialEnd: () -> Unit,
    navigateToTutorialEndPlus: () -> Unit,
    navigateToMainActivity: () -> Unit
) {
    val uiState by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initTutorial(isCodeTextEmpty, isFromOnBoarding)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            TutorialSideEffect.NavigateToTutorialEnd -> navigateToTutorialEnd()
            TutorialSideEffect.NavigateToTutorialEndPlus -> navigateToTutorialEndPlus()
            TutorialSideEffect.NavigateToMainActivity -> navigateToMainActivity()
        }
    }

    TutorialScreen(
        uiState = uiState,
        onScreenClicked = viewModel::onScreenClick
    )
}

@Composable
fun TutorialScreen(
    uiState: TutorialState,
    onScreenClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .clickable { onScreenClicked() }
    ) {
        Image(
            painter = painterResource(id = uiState.getCurrentTutorialImage()),
            contentDescription = "Tutorial Step ${uiState.currentScreen + 1}",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TutorialScreenPreview() {
    YelloTheme {
        TutorialScreen(
            uiState = TutorialState(currentScreen = 0)
        )
    }
}
