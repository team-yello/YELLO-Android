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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.el.yello.BuildConfig
import com.el.yello.R
import com.example.ui.compose.component.YelloSnackbar
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale600
import com.example.ui.compose.theme.Grayscale900
import com.example.ui.compose.theme.PretendardFontFamily
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
    successLogout: () -> Unit,
    navigateBack: () -> Unit,
    navigateCustomerSupport: () -> Unit,
    navigatePrivacyPolicy: () -> Unit,
    navigateTermsOfService: () -> Unit,
    navigateAccountDeletion: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect {
        when (it) {
            is SettingSideEffect.SuccessLogout -> {
                successLogout()
            }

            is SettingSideEffect.FailureLogout -> {
                snackbarHostState.showSnackbar(it.msg)
            }

            is SettingSideEffect.NavigateAccountDeletion -> {
                navigateAccountDeletion()
            }

            is SettingSideEffect.NavigateBack -> {
                navigateBack()
            }

            is SettingSideEffect.NavigateCustomerSupport -> {
                navigateCustomerSupport()
            }

            is SettingSideEffect.NavigatePrivacyPolicy -> {
                navigatePrivacyPolicy()
            }

            is SettingSideEffect.NavigateTermsOfService -> {
                navigateTermsOfService()
            }
        }
    }

    SettingScreen(
        snackbarHostState = snackbarHostState,
        onLogoutClick = viewModel::logout,
        onClickBack = viewModel::onClickBack,
        onCustomerSupportClick = viewModel::onCustomerSupportClick,
        onClickPrivacyPolicyClick = viewModel::onClickPrivacyPolicyClick,
        onClickTermsOfServiceClick = viewModel::onClickTermsOfServiceClick,
        onAccountDeletionClick = viewModel::onAccountDeletionClick
    )
}

@Composable
fun SettingScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onLogoutClick: () -> (Unit) = {},
    onClickBack: () -> (Unit) = {},
    onCustomerSupportClick: () -> (Unit) = {},
    onClickPrivacyPolicyClick: () -> (Unit) = {},
    onClickTermsOfServiceClick: () -> (Unit) = {},
    onAccountDeletionClick: () -> Unit = {}
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            SettingTopBar(onClickBack)
        },
        bottomBar = {
            SettingBottomBar(onAccountDeletionClick)
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 24.dp, start = 40.dp, end = 40.dp),
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
                onLogoutClick()
            }
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
            modifier = Modifier
                .padding(8.dp)
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