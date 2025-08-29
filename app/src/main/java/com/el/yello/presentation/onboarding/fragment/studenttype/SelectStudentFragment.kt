package com.el.yello.presentation.onboarding.fragment.studenttype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.contract.SelectStudentState
import com.el.yello.presentation.onboarding.screen.SelectStudentRoute
import com.el.yello.util.manager.AmplitudeManager
import com.example.domain.enums.StudentType
import com.example.ui.compose.theme.YelloTheme
import org.json.JSONObject

class SelectStudentFragment : Fragment() {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                var currentSelectedType by remember { mutableStateOf<StudentType?>(null) }
                
                viewModel.selectedStudentType.observe(viewLifecycleOwner) { selectedType ->
                    currentSelectedType = selectedType
                }
                
                YelloTheme {
                    SelectStudentRoute(
                        state = SelectStudentState(selectedStudentType = currentSelectedType),
                        onStudentTypeSelected = { studentType ->
                            viewModel.selectStudentType(studentType)
                        },
                        onNavigateToHighSchool = {
                            findNavController().navigate(R.id.action_selectStudentFragment_to_highschoolInfoFragment)
                            amplitudeSelectStudent()
                            (requireActivity() as OnBoardingActivity).progressBarPlus()
                        },
                        onNavigateToUniversity = {
                            findNavController().navigate(R.id.action_selectStudentFragment_to_universityInfoFragment)
                            amplitudeSelectStudent()
                            (requireActivity() as OnBoardingActivity).progressBarPlus()
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callParentActivity {
            hideBackBtn()
        }
    }

    private fun callParentActivity(callback: OnBoardingActivity.() -> Unit) {
        val activity = requireActivity()
        if (activity is OnBoardingActivity) {
            activity.callback()
        }
    }

    private fun amplitudeSelectStudent() {
        AmplitudeManager.trackEventWithProperties(
            EVENT_CLICK_ONBOARDING_NEXT,
            JSONObject().put(NAME_ONBOARD_VIEW, VALUE_STUDENT_TYPE),
        )
    }

    companion object {
        private const val EVENT_CLICK_ONBOARDING_NEXT = "click_onboarding_next"
        private const val NAME_ONBOARD_VIEW = "onboard_view"
        private const val VALUE_STUDENT_TYPE = "student_type"
    }
}
