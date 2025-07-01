package com.example.ui.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.compose.theme.Gray90
import com.example.ui.compose.theme.PretendardFontFamily
import com.example.ui.compose.theme.White

@Composable
fun YelloSnackbar(data: SnackbarData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gray90),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = data.visuals.message,
            fontFamily = PretendardFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = White,
            modifier = Modifier.padding(vertical = 15.dp)
        )
    }
}