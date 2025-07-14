package com.el.yello.presentation.getalarm.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.el.yello.R
import com.el.yello.presentation.getalarm.contract.GetAlarmSideEffect
import com.el.yello.presentation.getalarm.viewmodel.GetAlarmViewModel
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale500
import com.example.ui.compose.theme.PretendardFontFamily
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloMain500
import com.example.ui.compose.theme.YelloTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GetAlarmRoute(
    viewModel: GetAlarmViewModel = hiltViewModel(),
    isFromOnBoarding: Boolean,
    isCodeTextEmpty: Boolean,
    navigateToTutorial: () -> Unit
) {
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(isGranted)
    }

    LaunchedEffect(Unit) {
        viewModel.initGetAlarm(isFromOnBoarding, isCodeTextEmpty)
    }

    viewModel.collectSideEffect {
        when (it) {
            GetAlarmSideEffect.NavigateToTutorial -> navigateToTutorial()
        }
    }

    GetAlarmScreen(
        onStartYelloClick = {
            viewModel.onStartYelloClickEvent()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                ) {
                    viewModel.onPermissionResult(true)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                viewModel.onPermissionResult(true)
            }
        }
    )
}

@Composable
fun GetAlarmScreen(
    onStartYelloClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.height(104.dp)
            )

            // 제목, 하트 아이콘
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_tv_startapp_title),
                    fontSize = 24.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.ic_onboarding_startapp_heart),
                    contentDescription = "Heart Icon"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 부제목
            Text(
                text = stringResource(id = R.string.onboarding_tv_startapp_subtitle),
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                color = Grayscale500,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 알림 아이콘 이미지
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
                painter = painterResource(id = R.drawable.img_tutorial_startapp),
                contentDescription = "Start App Image",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(1f))

            // 쪽지 알림 받기 버튼
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 34.dp),
                onClick = onStartYelloClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = YelloMain500,
                    contentColor = Black
                ),
                shape = RoundedCornerShape(100.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 18.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_tv_start_yello),
                    fontSize = 15.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GetAlarmScreenPreview() {
    YelloTheme {
        GetAlarmScreen()
    }
}
