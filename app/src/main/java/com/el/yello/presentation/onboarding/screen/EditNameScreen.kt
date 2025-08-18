package com.el.yello.presentation.onboarding.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.el.yello.R
import com.el.yello.presentation.onboarding.contract.EditNameSideEffect
import com.el.yello.presentation.onboarding.contract.EditNameState
import com.el.yello.presentation.onboarding.viewmodel.EditNameViewModel
import com.example.ui.compose.component.YelloSnackbar
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale500
import com.example.ui.compose.theme.Grayscale600
import com.example.ui.compose.theme.Grayscale700
import com.example.ui.compose.theme.Grayscale800
import com.example.ui.compose.theme.SemanticRed500
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloMain500
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.compose.theme.yTypography
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun EditNameRoute(
    viewModel: EditNameViewModel = hiltViewModel(),
    kakaoId: Long,
    userName: String,
    profileImageUrl: String,
    email: String,
    gender: String,
    navigateToOnBoarding: (Long, String, String, String, String) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.initUserData(kakaoId, userName, gender, email, profileImageUrl)
    }

    val state by viewModel.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val backPressedTime = remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime.longValue >= 2000L) {
            backPressedTime.longValue = currentTime
            coroutineScope.launch {
                snackBarHostState.showSnackbar("한 번 더 누르면 종료됩니다.")
            }
        } else {
            navigateBack()
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is EditNameSideEffect.NavigateToOnBoarding -> {
                navigateToOnBoarding(sideEffect.kakaoId, sideEffect.name, sideEffect.profileImage, sideEffect.email, sideEffect.gender)
            }

            is EditNameSideEffect.ShowSnackBar -> {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(sideEffect.message)
                }
            }
        }
    }

    EditNameScreen(
        modifier = Modifier.fillMaxSize(),
        state = state,
        snackBarHostState = snackBarHostState,
        onNameTextChanged = viewModel::onNameTextChanged,
        onDeleteClick = viewModel::onDeleteClick,
        onConfirmClick = viewModel::onConfirmClick
    )
}

@Composable
fun EditNameScreen(
    modifier: Modifier = Modifier,
    state: EditNameState,
    snackBarHostState: SnackbarHostState,
    onNameTextChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Black),
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 24.dp, start = 40.dp, end = 40.dp),
                hostState = snackBarHostState,
                snackbar = { YelloSnackbar(data = it) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // 상단 말풍선 이미지
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    painter = painterResource(id = R.drawable.img_onboarding_edit_name_bubble),
                    contentDescription = "bubble"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 제목
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(id = R.string.onboarding_tv_name),
                style = yTypography.headline01,
                color = White,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 이름 입력 필드
            EditNameTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = state.nameText,
                isValid = state.isValidName,
                onTextChanged = onNameTextChanged,
                onDeleteClick = onDeleteClick,
            )

            Spacer(modifier = Modifier.height(4.dp))

            EditNameDescriptionText(
                modifier = Modifier.padding(horizontal = 16.dp),
                isValid = state.isValidName,
                text = state.nameText,
            )

            Spacer(modifier = Modifier.weight(1f))

            // 수정 완료 버튼
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = onConfirmClick,
                enabled = state.isConfirmButtonEnabled,
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isConfirmButtonEnabled) YelloMain500 else Grayscale800,
                    contentColor = if (state.isConfirmButtonEnabled) Black else Grayscale700,
                    disabledContainerColor = Grayscale800,
                    disabledContentColor = Grayscale700
                ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_btn_edit_name_next),
                    style = yTypography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun EditNameTextField(
    modifier: Modifier = Modifier,
    text: String,
    isValid: Boolean,
    onTextChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    text.isEmpty() -> Grayscale800
                    isValid -> Grayscale800
                    else -> SemanticRed500.copy(alpha = 0.15f)
                }
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = onTextChanged,
                textStyle = yTypography.bodyLarge.copy(color = White),
                cursorBrush = SolidColor(White),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box {
                        if (text.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.onboarding_name_id_name_hint),
                                style = yTypography.bodyLarge,
                                color = Grayscale600
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (text.isNotBlank()) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onDeleteClick() },
                    painter = painterResource(
                        id = if (isValid) R.drawable.ic_onboarding_delete
                        else R.drawable.ic_onboarding_delete_red
                    ),
                    contentDescription = "delete",
                )
            }
        }
    }
}

@Composable
private fun EditNameDescriptionText(
    modifier: Modifier = Modifier,
    isValid: Boolean,
    text: String = "",
) {
    if (text.isBlank()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isValid) {
                stringResource(id = R.string.onboarding_tv_name_description_first)
            } else {
                stringResource(id = R.string.onboarding_name_id_hangul_error)
            },
            style = yTypography.labelLarge,
            color = if (isValid) Grayscale500 else SemanticRed500
        )

        if (isValid) {
            Text(
                modifier = Modifier.padding(start = 3.dp),
                text = stringResource(id = R.string.onboarding_tv_name_description_second),
                style = yTypography.labelLarge,
                color = YelloMain500,
            )

            Text(
                text = stringResource(id = R.string.onboarding_tv_name_description_third),
                style = yTypography.labelLarge,
                color = Grayscale500
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditNameScreenEmptyPreview() {
    YelloTheme {
        EditNameScreen(
            state = EditNameState(
                nameText = "",
                isValidName = false,
                checkNameLength = false
            ),
            snackBarHostState = SnackbarHostState(),
            onNameTextChanged = {},
            onDeleteClick = {},
            onConfirmClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditNameScreenPreview() {
    YelloTheme {
        EditNameScreen(
            state = EditNameState(
                nameText = "테스트",
                isValidName = true,
                checkNameLength = true
            ),
            snackBarHostState = SnackbarHostState(),
            onNameTextChanged = {},
            onDeleteClick = {},
            onConfirmClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditNameScreenErrorPreview() {
    YelloTheme {
        EditNameScreen(
            state = EditNameState(
                nameText = "test123",
                isValidName = false,
                checkNameLength = true
            ),
            snackBarHostState = SnackbarHostState(),
            onNameTextChanged = {},
            onDeleteClick = {},
            onConfirmClick = {}
        )
    }
}
