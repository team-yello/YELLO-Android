package com.el.yello.presentation.setting.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.el.yello.R
import com.example.ui.compose.theme.Grayscale600
import com.example.ui.compose.theme.Grayscale900
import com.example.ui.compose.theme.SemanticRed500
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.compose.theme.yTypography
import com.example.ui.util.noRippleClickable


@Composable
fun ProfileQuitDialog(
    onRejectClick: () -> Unit,
    onQuitClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val dialogWidth = screenWidth - 60.dp // 좌우 각각 40dp 여백
    
    Dialog(
        onDismissRequest = onRejectClick,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = dialogWidth)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Grayscale900
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.profile_dialog_tv_title),
                    style = yTypography.subtitle02,
                    color = White,
                    modifier = Modifier.padding(top = 40.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 27.dp, end = 27.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.profile_dialog_btn_no),
                        style = yTypography.button,
                        color = Grayscale600,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            .noRippleClickable { onRejectClick() }
                    )

                    Text(
                        text = stringResource(id = R.string.profile_dialog_btn_yes),
                        style = yTypography.button,
                        color = SemanticRed500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            .noRippleClickable { onQuitClick() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileQuitDialogPreview() {
    YelloTheme {
        ProfileQuitDialog(
            onRejectClick = {},
            onQuitClick = {}
        )
    }
}
