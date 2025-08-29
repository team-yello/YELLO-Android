package com.el.yello.presentation.onboarding.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.el.yello.R
import com.el.yello.presentation.onboarding.component.StudentTypeButton
import com.el.yello.presentation.onboarding.contract.SelectStudentState
import com.example.domain.enums.StudentType
import com.example.ui.compose.component.YelloSnackbar
import com.example.ui.compose.theme.Black
import com.example.ui.compose.theme.Grayscale700
import com.example.ui.compose.theme.Grayscale800
import com.example.ui.compose.theme.White
import com.example.ui.compose.theme.YelloMain500
import com.example.ui.compose.theme.YelloTheme
import com.example.ui.compose.theme.yTypography

@Composable
fun SelectStudentRoute(
    modifier: Modifier = Modifier,
    state: SelectStudentState,
    onStudentTypeSelected: (StudentType) -> Unit,
    onNavigateToHighSchool: () -> Unit,
    onNavigateToUniversity: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    SelectStudentScreen(
        modifier = modifier,
        state = state,
        snackBarHostState = snackBarHostState,
        onStudentTypeSelected = onStudentTypeSelected,
        onNavigateToHighSchool = onNavigateToHighSchool,
        onNavigateToUniversity = onNavigateToUniversity
    )
}

@Composable
fun SelectStudentScreen(
    modifier: Modifier = Modifier,
    state: SelectStudentState,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onStudentTypeSelected: (StudentType) -> Unit = {},
    onNavigateToHighSchool: () -> Unit = {},
    onNavigateToUniversity: () -> Unit = {}
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
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.onboarding_select_student_Type_title),
                style = yTypography.headline02,
                color = White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            StudentTypeButton(
                title = stringResource(id = R.string.onboarding_tv_select_student_highschool),
                defaultIcon = R.drawable.ic_student_highschool_face_default,
                selectedIcon = R.drawable.ic_student_highschool_face_select,
                unselectedIcon = R.drawable.ic_student_highschool_face_unselected,
                isSelected = state.selectedStudentType == StudentType.SCHOOL,
                isOtherSelected = state.selectedStudentType == StudentType.UNIVERSITY,
                onButtonClick = { onStudentTypeSelected(StudentType.SCHOOL) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            StudentTypeButton(
                title = stringResource(id = R.string.onboarding_tv_select_student_university),
                defaultIcon = R.drawable.ic_student_university_face_default,
                selectedIcon = R.drawable.ic_student_university_face_select,
                unselectedIcon = R.drawable.ic_student_university_face_unselected,
                isSelected = state.selectedStudentType == StudentType.UNIVERSITY,
                isOtherSelected = state.selectedStudentType == StudentType.SCHOOL,
                onButtonClick = { onStudentTypeSelected(StudentType.UNIVERSITY) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    state.selectedStudentType?.let { studentType ->
                        when (studentType) {
                            StudentType.SCHOOL -> {
                                onNavigateToHighSchool()
                            }

                            StudentType.UNIVERSITY -> {
                                onNavigateToUniversity()
                            }
                        }
                    }
                },
                enabled = state.selectedStudentType != null,
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.selectedStudentType != null) YelloMain500 else Grayscale800,
                    contentColor = if (state.selectedStudentType != null) Black else Grayscale700,
                    disabledContainerColor = Grayscale800,
                    disabledContentColor = Grayscale700
                ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_btn_gender_done),
                    style = yTypography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(34.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectStudentScreenPreview() {
    YelloTheme {
        SelectStudentScreen(
            state = SelectStudentState(selectedStudentType = StudentType.SCHOOL)
        )
    }
}
