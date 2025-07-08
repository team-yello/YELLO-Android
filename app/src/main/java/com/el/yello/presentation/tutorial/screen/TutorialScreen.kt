package com.el.yello.presentation.tutorial.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.el.yello.presentation.tutorial.contract.TutorialSideEffect
import com.el.yello.presentation.tutorial.contract.TutorialState
import com.el.yello.presentation.tutorial.viewmodel.TutorialViewModel
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
    navigateToMainActivity: () -> Unit
) {
    val uiState by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initTutorial(isCodeTextEmpty, isFromOnBoarding)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            TutorialSideEffect.NavigateToTutorialEnd -> navigateToTutorialEnd()
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
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) { paddingValues ->
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable { onScreenClicked() },
            painter = painterResource(id = uiState.getCurrentTutorialImage()),
            contentDescription = "Tutorial Step ${uiState.currentScreen + 1}",
            contentScale = ContentScale.FillBounds,
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
