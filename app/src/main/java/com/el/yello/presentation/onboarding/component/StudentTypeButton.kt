package com.el.yello.presentation.onboarding.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.el.yello.R
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale500
import com.example.ui.compose.theme.Grayscale700
import com.example.ui.compose.theme.YelloMain500
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.compose.theme.yTypography

@Composable
fun StudentTypeButton(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes defaultIcon: Int,
    @DrawableRes selectedIcon: Int,
    @DrawableRes unselectedIcon: Int = defaultIcon,
    isSelected: Boolean,
    isOtherSelected: Boolean = false,
    onButtonClick: () -> Unit,
) {
    Card(
        onClick = onButtonClick,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) YelloMain500 else Grayscale700,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Black),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(
                        id = if (isSelected) {
                            R.drawable.ic_student_type_check_selected
                        } else {
                            R.drawable.ic_student_type_check_unselected
                        }
                    ),
                    contentDescription = if (isSelected) "선택됨" else "선택 안됨",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // 옐로 아이콘
            Image(
                painter = painterResource(
                    id = when {
                        isSelected -> selectedIcon
                        isOtherSelected -> unselectedIcon
                        else -> defaultIcon
                    }
                ),
                contentDescription = "$title 아이콘",
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 제목
            Text(
                text = title,
                style = yTypography.subtitle02,
                color = when {
                    isSelected -> YelloMain500
                    isOtherSelected -> Grayscale700
                    else -> Grayscale500
                },
                modifier = Modifier.padding(vertical = 3.dp)
            )

            Spacer(modifier = Modifier.height(21.dp))
        }
    }
}

@Preview
@Composable
fun StudentTypeCardSelectedPreview() {
    YelloTheme {
        StudentTypeButton(
            title = "중/고등학생",
            defaultIcon = R.drawable.ic_student_highschool_face_default,
            selectedIcon = R.drawable.ic_student_highschool_face_select,
            unselectedIcon = R.drawable.ic_student_highschool_face_unselected,
            isSelected = true,
            isOtherSelected = false,
            onButtonClick = {},
        )
    }
}

@Preview
@Composable
fun StudentTypeCardUnselectedPreview() {
    YelloTheme {
        StudentTypeButton(
            title = "대학생",
            defaultIcon = R.drawable.ic_student_university_face_default,
            selectedIcon = R.drawable.ic_student_university_face_select,
            unselectedIcon = R.drawable.ic_student_university_face_unselected,
            isSelected = false,
            isOtherSelected = true,
            onButtonClick = {},
        )
    }
}
