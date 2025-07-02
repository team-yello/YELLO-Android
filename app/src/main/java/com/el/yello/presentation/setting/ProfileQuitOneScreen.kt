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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.el.yello.R
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale500
import com.example.ui.compose.theme.Grayscale700
import com.example.ui.compose.theme.Grayscale800
import com.example.ui.compose.theme.Grayscale900
import com.example.ui.compose.theme.PretendardFontFamily
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloMain500
import com.example.ui.compose.theme.yTypography
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileQuitOneRoute(
    viewModel: ProfileQuitOneViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToProfileQuitTwo: () -> Unit
) {
    viewModel.collectSideEffect {
        when (it) {
            is ProfileQuitOneSideEffect.NavigateBack -> {
                navigateBack()
            }

            is ProfileQuitOneSideEffect.NavigateToProfileQuitTwo -> {
                navigateToProfileQuitTwo()
            }
        }
    }

    ProfileQuitOneScreen(
        onBackClick = viewModel::onBackClick,
        onReturnClick = viewModel::onBackClick,
        onQuitClick = viewModel::onQuitClick
    )
}

@Composable
fun ProfileQuitOneScreen(
    onBackClick: () -> Unit = {},
    onReturnClick: () -> Unit = {},
    onQuitClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        topBar = {
            ProfileQuitOneTopBar(onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Black)
                .verticalScroll(rememberScrollState())
        ) {
            // 경고 아이콘
            Image(
                painter = painterResource(id = R.drawable.ic_warning),
                contentDescription = "Warning Icon",
                modifier = Modifier
                    .padding(top = 25.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // 경고 제목
            Text(
                text = stringResource(id = R.string.profile_quit_for_sure_tv_subtitle_1),
                style = yTypography.headline00,
                color = YelloMain500,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )

            // 경고 부제목 1
            Text(
                text = stringResource(id = R.string.profile_quit_for_sure_tv_subtitle_2_top),
                style = yTypography.bodySmall,
                color = Grayscale500,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
            )

            // 경고 부제목 2
            Text(
                text = stringResource(id = R.string.profile_quit_for_sure_tv_subtitle_2_bottom),
                style = yTypography.bodySmall,
                color = Grayscale500,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 4.dp)
                    .fillMaxWidth()
            )

            // 상단 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Grayscale900)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 26.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.profile_quit_upper_title),
                        style = yTypography.subtitle01,
                        color = White,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 26.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.img_point_zero),
                        contentDescription = "Point Zero",
                        modifier = Modifier.padding(top = 18.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.profile_quit_upper_body_1),
                        style = yTypography.labelMedium,
                        color = White,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.profile_quit_upper_body_2),
                        style = yTypography.labelMedium,
                        color = White,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 하단 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Grayscale900)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 36.dp)
                ) {
                    // 이유 1
                    Text(
                        text = stringResource(id = R.string.profile_quit_for_sure_tv_reason_1),
                        style = yTypography.subtitle01,
                        color = White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .padding(top = 36.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.img_quit_for_sure_1),
                        contentDescription = "Quit Reason 1",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 42.dp)
                            .padding(top = 6.dp)
                    )

                    // 이유 2
                    Text(
                        text = stringResource(id = R.string.profile_quit_for_sure_tv_reason_2),
                        style = yTypography.subtitle01,
                        color = White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .padding(top = 40.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.img_quit_for_sure_2),
                        contentDescription = "Quit Reason 2",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 42.dp)
                            .padding(top = 6.dp)
                    )

                    // 이유 3
                    Text(
                        text = stringResource(id = R.string.profile_quit_for_sure_tv_reason_3),
                        style = yTypography.subtitle01,
                        color = White,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .padding(top = 40.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.img_quit_for_sure_3),
                        contentDescription = "Quit Reason 3",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 42.dp)
                            .padding(top = 6.dp)
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 60.dp),
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
                    text = stringResource(id = R.string.profile_quit_btn_resume),
                    fontFamily = PretendardFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            // 돌아가기 버튼
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 34.dp),
                onClick = { onReturnClick() },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Grayscale800,
                    contentColor = YelloMain500
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.profile_quit_btn_return),
                    fontFamily = PretendardFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ProfileQuitOneTopBar(
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
            text = stringResource(id = R.string.profile_quit_for_sure_tv_title),
            fontFamily = PretendardFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = White
        )
    }
}
