package com.el.yello.presentation.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale600
import com.example.ui.compose.theme.Grayscale700
import com.example.ui.compose.theme.Grayscale800
import com.example.ui.compose.theme.Grayscale900
import com.example.ui.compose.theme.PretendardFontFamily
import com.example.ui.compose.theme.SemanticRed500
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.compose.theme.yTypography
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileReasonRoute(
    viewModel: ProfileReasonViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    navigateToMainActivity: () -> Unit,
    navigateToReStart: () -> Unit,
    showToast: (String) -> Unit

) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is ProfileReasonSideEffect.NavigateBack -> {
                onBackClick()
            }

            is ProfileReasonSideEffect.NavigateToMainActivity -> {
                navigateToMainActivity()
            }

            is ProfileReasonSideEffect.SuccessKaKaoUnlink -> {
                navigateToReStart()
            }
            is ProfileReasonSideEffect.ShowToast -> showToast(it.message)
        }
    }

    if (state.isShowProfileQuitDialog) {
        com.el.yello.presentation.setting.dialog.ProfileQuitDialog(
            onRejectClick = viewModel::onDismissProfileQuitDialog,
            onQuitClick = viewModel::deleteUserDataToServer
        )
    }

    if (state.isShowProfileQuitInviteFriendDialog) {
        com.el.yello.presentation.setting.dialog.ProfileQuitInviteFriendDialog(
            onNoClick = viewModel::onDismissProfileQuitInviteFriendDialog,
            onYesClick = viewModel::navigateToMainActivity
        )
    }


    ProfileReasonScreen(
        etcText = state.etcText,
        selectedReasonIndex = state.selectedReasonIndex,
        isEnableButton = state.isEnableButton,
        reasonList = state.reasonList,
        onReasonSelected = viewModel::selectReason,
        onEtcTextChanged = viewModel::updateEtcText,
        onBackClick = viewModel::onBackClick,
        onCompleteClick = viewModel::onCompleteClick
    )
}

@Composable
fun ProfileReasonScreen(
    etcText: String = "",
    selectedReasonIndex: Int = -1,
    isEnableButton: Boolean = false,
    reasonList: List<String> = emptyList(),
    onReasonSelected: (Int) -> Unit = {},
    onEtcTextChanged: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .systemBarsPadding(),
        topBar = {
            ProfileQuitTopBar(onBackClick)
        },
        bottomBar = {
            ProfileReasonBottomBar(
                isEnableButton = isEnableButton,
                onCompleteClick = onCompleteClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Black)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = stringResource(id = R.string.profile_quit_reason_title),
                style = yTypography.headline01,
                color = White
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                reasonList.forEachIndexed { index, reason ->
                    val isEtc = index == reasonList.size - 1 // 마지막 항목이 "기타"인 경우

                    ProfileReasonItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .padding(bottom = 4.dp)
                            .clickable { onReasonSelected(index) },
                        content = reason,
                        etcText = if (isEtc) etcText else "",
                        isSelected = index == selectedReasonIndex,
                        isEtc = isEtc && index == selectedReasonIndex,
                        onEtcChange = onEtcTextChanged
                    )

                    if (isEtc) Spacer(modifier = Modifier.height(22.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileReasonItem(
    modifier: Modifier = Modifier,
    content: String,
    etcText: String,
    isSelected: Boolean,
    isEtc: Boolean,
    onEtcChange: (String) -> Unit
) {
    Box(
        modifier = modifier
            .then(
                if (isSelected) {
                    Modifier.border(BorderStroke(1.dp, Color.White), RoundedCornerShape(8.dp))
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Black else Grayscale900)
            .padding(20.dp),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(if (isSelected) R.drawable.ic_profile_quit_reason_check else R.drawable.ic_profile_quit_reason_uncheck),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = content,
                    style = yTypography.bodyLarge,
                    color = White
                )
            }

            if (isEtc) {
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Grayscale800)
                        .fillMaxWidth()
                        .height(76.dp)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    BasicTextField(
                        value = etcText,
                        onValueChange = onEtcChange,
                        textStyle = yTypography.body02.copy(color = White),
                        decorationBox = { innerTextField ->
                            if (etcText.isBlank()) {
                                Text(
                                    text = "사유를 적어주세요.(최대 30자)",
                                    style = yTypography.body02,
                                    color = Grayscale600
                                )
                            }
                            innerTextField()
                        },
                        singleLine = true,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun ProfileReasonBottomBar(
    isEnableButton: Boolean,
    onCompleteClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black)
            .padding(horizontal = 16.dp)
            .padding(bottom = 34.dp),
        onClick = { onCompleteClick() },
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = if (isEnableButton) Grayscale600 else Grayscale700
        ),
        border = BorderStroke(1.dp, Grayscale700),
        contentPadding = PaddingValues(vertical = 16.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
        enabled = isEnableButton
    ) {
        Text(
            text = stringResource(id = R.string.profile_quit_reason_button),
            fontFamily = PretendardFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = if (isEnableButton) SemanticRed500 else Grayscale600,
        )
    }
}

@Preview
@Composable
fun ProfileReasonScreenPreview() {
    YelloTheme {
        ProfileReasonScreen()
    }
}

@Preview
@Composable
fun ProfileReasonItemPreview() {
    YelloTheme {
        ProfileReasonItem(
            isSelected = false,
            content = "앱에 아는 사람들이 없어서",
            isEtc = true,
            etcText = "2",
            onEtcChange = {})
    }
}