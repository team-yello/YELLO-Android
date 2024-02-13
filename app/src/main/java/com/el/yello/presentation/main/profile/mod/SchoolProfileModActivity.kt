package com.el.yello.presentation.main.profile.mod

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileSchoolModBinding
import com.el.yello.presentation.main.profile.mod.UnivProfileModViewModel.Companion.TEXT_NONE
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.activity.navigateTo
import com.example.ui.base.BindingActivity
import com.example.ui.context.drawableOf
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SchoolProfileModActivity :
    BindingActivity<ActivityProfileSchoolModBinding>(R.layout.activity_profile_school_mod) {

    private val viewModel by viewModels<SchoolProfileModViewModel>()

    private var schoolModSearchBottomSheet: SchoolModSearchBottomSheet? = null
    private var schoolModGradeBottomSheet: SchoolModGradeBottomSheet? = null
    private var schoolModClassroomBottomSheet: SchoolModClassroomBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUserData()
        initChangeToUnivBtnListener()
        initSaveBtnListener()
        initBackBtnListener()
        initBottomSheetListener()
        observeGetUserDataResult()
        observeGetIsModValidResult()
        observeGetSchoolGroupIdResult()
        observePostNewProfileResult()
        observeTextIsNone()
    }

    private fun initUserData() {
        binding.vm = viewModel
        viewModel.getUserDataFromServer()
        viewModel.getIsModValidFromServer()
    }

    private fun initChangeToUnivBtnListener() {
        binding.btnChangeSchoolToUniv.setOnSingleClickListener {
            navigateTo<UnivProfileModActivity>()
            finish()
        }
    }

    private fun initSaveBtnListener() {
        binding.btnProfileModSave.setOnSingleClickListener {
            when {
                !viewModel.isChanged -> {
                    yelloSnackbar(binding.root, getString(R.string.profile_mod_no_change))
                }

                !viewModel.isModAvailable -> {
                    yelloSnackbar(binding.root, getString(R.string.profile_mod_no_valid))
                }

                viewModel.grade.value == TEXT_NONE -> {
                    binding.layoutProfileModGrade.background =
                        drawableOf(R.drawable.shape_red_fill_red500_line_10_rect)
                    binding.tvProfileModSubgroupError.isVisible = true
                }

                viewModel.classroom.value == TEXT_NONE -> {
                    binding.layoutProfileModClassroom.background =
                        drawableOf(R.drawable.shape_red_fill_red500_line_10_rect)
                    binding.tvProfileModClassroomError.isVisible = true
                }

                else -> {
                    viewModel.getSchoolGroupIdFromServer()
                }
            }
        }
    }

    private fun initBackBtnListener() {
        binding.btnProfileModBack.setOnSingleClickListener { finish() }
    }

    private fun initBottomSheetListener() {
        binding.btnSearchSchool.setOnSingleClickListener {
            schoolModSearchBottomSheet = SchoolModSearchBottomSheet.newInstance()
            schoolModSearchBottomSheet?.show(supportFragmentManager, DIALOG_SCHOOL)
        }
        binding.btnSearchGrade.setOnSingleClickListener {
            schoolModGradeBottomSheet = SchoolModGradeBottomSheet.newInstance()
            schoolModGradeBottomSheet?.show(supportFragmentManager, DIALOG_GRADE)
        }
        binding.btnSearchClassroom.setOnSingleClickListener {
            schoolModClassroomBottomSheet = SchoolModClassroomBottomSheet.newInstance()
            schoolModClassroomBottomSheet?.show(supportFragmentManager, DIALOG_CLASSROOM)
        }
    }

    private fun observeGetUserDataResult() {
        viewModel.getUserDataResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
        }.launchIn(lifecycleScope)
    }

    private fun observeGetIsModValidResult() {
        viewModel.getIsModValidState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.tvProfileModLastDateTitle.visibility = View.VISIBLE
                }

                is UiState.Empty -> {
                    binding.tvProfileModLastDateTitle.visibility = View.INVISIBLE
                }

                is UiState.Failure -> yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeGetSchoolGroupIdResult() {
        viewModel.getSchoolGroupIdResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
        }.launchIn(lifecycleScope)
    }

    private fun observePostNewProfileResult() {
        viewModel.postToModProfileResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (result) {
                toast(getString(R.string.profile_mod_success))
                finish()
            } else {
                yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeTextIsNone() {
        viewModel.grade.observe(this) { grade ->
            binding.tvProfileModGradeText.isVisible = grade != TEXT_NONE
        }
        viewModel.classroom.observe(this) { classroom ->
            binding.tvProfileModClassroomText.isVisible = classroom != TEXT_NONE
        }
    }

    fun checkIsGradeTextNone() {
        if (viewModel.grade.value != TEXT_NONE) {
            binding.layoutProfileModGrade.background =
                drawableOf(R.drawable.shape_grayscales900_fill_12_rect)
            binding.tvProfileModSubgroupError.isVisible = false
        }
    }

    fun checkIsClassroomTextNone() {
        if (viewModel.classroom.value != TEXT_NONE) {
            binding.layoutProfileModClassroom.background =
                drawableOf(R.drawable.shape_grayscales900_fill_12_rect)
            binding.tvProfileModClassroomError.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (schoolModSearchBottomSheet != null) schoolModSearchBottomSheet?.dismiss()
        if (schoolModGradeBottomSheet != null) schoolModGradeBottomSheet?.dismiss()
        if (schoolModClassroomBottomSheet != null) schoolModClassroomBottomSheet?.dismiss()
    }

    private companion object {
        const val DIALOG_SCHOOL = "school"
        const val DIALOG_GRADE = "grade"
        const val DIALOG_CLASSROOM = "classroom"
    }
}