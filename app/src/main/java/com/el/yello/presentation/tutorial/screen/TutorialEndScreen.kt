package com.el.yello.presentation.tutorial.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.el.yello.R
import com.el.yello.presentation.tutorial.contract.TutorialEndSideEffect
import com.el.yello.presentation.tutorial.contract.TutorialEndState
import com.el.yello.presentation.tutorial.viewmodel.TutorialEndViewModel
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale500
import com.example.ui.compose.theme.PretendardFontFamily
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloMain500
import com.example.ui.compose.theme.YelloTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun TutorialEndRoute(
    viewModel: TutorialEndViewModel = hiltViewModel(),
    isPlus: Boolean,
    navigateToMainActivity: () -> Unit
) {
    val uiState by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initTutorialEnd(isPlus)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            TutorialEndSideEffect.NavigateToMainActivity -> navigateToMainActivity()
        }
    }

    TutorialEndScreen(
        uiState = uiState,
        onEndButtonClicked = viewModel::onEndButtonClick
    )
}

@Composable
fun TutorialEndScreen(
    uiState: TutorialEndState,
    onEndButtonClicked: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        bottomBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 34.dp)
                    .clickable { onEndButtonClicked() },
                shape = RoundedCornerShape(100.dp),
                colors = CardDefaults.cardColors(containerColor = YelloMain500)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.tutorial_tv_end_btn),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontFamily = PretendardFontFamily
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_tutorial_yelloface),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(96.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tutorial_point_p),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(id = R.string.tutorial_end_title_point),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    fontFamily = PretendardFontFamily
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.tutorial_end_subtitle),
                fontSize = 14.sp,
                color = Grayscale500,
                fontFamily = PretendardFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = uiState.getTutorialEndImage()),
                contentDescription = "Tutorial Point Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(42.dp))

            Text(
                text = stringResource(id = R.string.tutorial_third_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = White,
                fontFamily = PretendardFontFamily,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TutorialEndScreenPreview() {
    YelloTheme {
        TutorialEndScreen(
            uiState = TutorialEndState(isPlus = false)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TutorialEndScreenPlusPreview() {
    YelloTheme {
        TutorialEndScreen(
            uiState = TutorialEndState(isPlus = true)
        )
    }
}
