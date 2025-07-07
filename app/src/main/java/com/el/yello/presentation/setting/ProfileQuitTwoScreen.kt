package com.el.yello.presentation.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.el.yello.R
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale500
import com.example.ui.compose.theme.Grayscale600
import com.example.ui.compose.theme.Grayscale700
import com.example.ui.compose.theme.Grayscale800
import com.example.ui.compose.theme.PretendardFontFamily
import com.example.ui.compose.theme.SemanticRed500
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloMain500
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.compose.theme.yTypography
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileQuitTwoRoute(
    viewModel: ProfileQuitTwoViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToProfileQuitReason: () -> Unit
) {
    viewModel.collectSideEffect {
        when (it) {
            is ProfileQuitTwoSideEffect.NavigateBack -> {
                navigateBack()
            }

            is ProfileQuitTwoSideEffect.NavigateToProfileQuitReason -> {
                navigateToProfileQuitReason()
            }
        }
    }

    ProfileQuitTwoScreen(
        onBackClick = viewModel::onBackClick,
        onQuitClick = viewModel::onQuitClick
    )
}

@Composable
fun ProfileQuitTwoScreen(
    onBackClick: () -> Unit = {},
    onQuitClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        topBar = {
            ProfileQuitTwoTopBar(onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Black)
                .verticalScroll(rememberScrollState())
        ) {
            // 제목
            Text(
                text = stringResource(id = R.string.profile_quit_tv_subtitle_1),
                style = yTypography.headline00,
                color = White,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .padding(top = 40.dp)
                    .fillMaxWidth()
            )

            // 부제목 1
            Text(
                text = stringResource(id = R.string.profile_quit_tv_subtitle_2_top),
                style = yTypography.bodySmall,
                color = Grayscale500,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
                modifier = Modifier
                    .padding(horizontal = 62.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )

            // 부제목 2
            Text(
                text = stringResource(id = R.string.profile_quit_tv_subtitle_2_bottom),
                style = yTypography.bodySmall,
                color = Grayscale500,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
                modifier = Modifier
                    .padding(horizontal = 62.dp)
                    .fillMaxWidth()
            )

            // 이미지
            Image(
                painter = painterResource(id = R.drawable.img_quit),
                contentDescription = "Quit Image",
                modifier = Modifier
                    .padding(horizontal = 56.dp)
                    .padding(top = 27.dp)
                    .wrapContentSize()
                    .weight(1f)
            )

            // 규칙 텍스트
            Text(
                text = stringResource(id = R.string.profile_quit_for_sure_tv_rule),
                style = yTypography.labelMedium,
                color = Grayscale600,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 43.dp, bottom = 36.dp)
                    .fillMaxWidth()
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 34.dp),
                onClick = { onQuitClick() },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Black,
                    contentColor = Grayscale500
                ),
                border = BorderStroke(1.dp, Grayscale700),
                contentPadding = PaddingValues(vertical = 16.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.profile_quit_for_sure_btn),
                    fontFamily = PretendardFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = SemanticRed500,
                )
            }
        }
    }
}

@Composable
private fun ProfileQuitTwoTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black)
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = "Back",
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .clickable { onBackClick() }
        )

        Text(
            text = stringResource(id = R.string.profile_quit_tv_title),
            style = yTypography.subtitle01,
            color = White
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProfileQuitTwoScreenPreview() {
    YelloTheme {
        ProfileQuitTwoScreen()
    }
}