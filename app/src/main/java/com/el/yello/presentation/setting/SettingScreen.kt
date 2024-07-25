package com.el.yello.presentation.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import com.el.yello.BuildConfig
import com.el.yello.R
import com.el.yello.presentation.setting.SettingsActivity.Companion.CLICK_PROFILE_LOGOUT
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.compose.component.YelloSnackbar
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale600
import com.example.ui.compose.theme.Grayscale900
import com.example.ui.compose.theme.PretendardFontFamily
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.state.UiState
import com.example.ui.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onClickBack: () -> (Unit) = {},
    onCustomerSupportClick: () -> (Unit) = {},
    onClickPrivacyPolicyClick: () -> (Unit) = {},
    onClickTermsOfServiceClick: () -> (Unit) = {},
    onAccountDeletionClick: () -> Unit = {}
) {
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
    val kakaoLogoutState = viewModel.kakaoLogoutState.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SettingTopBar(onClickBack)
        },
        bottomBar = {
            SettingBottomBar(onAccountDeletionClick)
        },
        snackbarHost = {
            androidx.compose.material3.SnackbarHost(
                modifier = Modifier.padding(bottom = 70.dp, start = 40.dp, end = 40.dp),
                hostState = snackbarHostState,
                snackbar = {
                    YelloSnackbar(data = it)
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            SettingCard("고객센터") {
                onCustomerSupportClick()
            }
            Spacer(modifier = Modifier.height(4.dp))
            SettingCard("개인정보 처리방침") {
                onClickPrivacyPolicyClick()
            }
            Spacer(modifier = Modifier.height(4.dp))
            SettingCard("이용약관") {
                onClickTermsOfServiceClick()
            }
            Spacer(modifier = Modifier.height(4.dp))
            SettingCard("로그아웃") {
                AmplitudeManager.trackEventWithProperties(CLICK_PROFILE_LOGOUT)
                viewModel.logoutKakaoAccount()
            }
        }
        when (kakaoLogoutState.value) {
            is UiState.Success -> {
                AmplitudeManager.trackEventWithProperties(SettingsActivity.COMPLETE_PROFILE_LOGOUT)
                Utils.restartApp(LocalContext.current, null)
            }

            is UiState.Failure -> {
                LaunchedEffect(key1 = true) {
                    snackbarHostState.showSnackbar("오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                }
            }
            else -> { }
        }
    }
}


@Composable
private fun SettingCard(
    title: String,
    onClickCard: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClickCard() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Grayscale900)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 21.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = White,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun SettingTopBar(
    onClickBack: () -> (Unit)
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClickBack()
            },
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = "뒤로가기"
        )
        Text(
            text = "설정",
            fontFamily = PretendardFontFamily,
            fontSize = 18.sp,
            color = White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun SettingBottomBar(
    onAccountDeletionClick: () -> (Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "버전 ${BuildConfig.VERSION_NAME}",
            fontFamily = PretendardFontFamily,
            fontSize = 13.sp,
            color = Grayscale600,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "계정 탈퇴",
            fontFamily = PretendardFontFamily,
            fontSize = 13.sp,
            color = Grayscale600,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(8.dp)
                .clickable {
                    onAccountDeletionClick()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    YelloTheme {
        SettingScreen()
    }
}