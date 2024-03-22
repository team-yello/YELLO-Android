package com.el.yello.presentation.main.profile.schoolmod

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileSchoolModBinding
import com.el.yello.presentation.main.profile.detail.ProfileDetailActivity
import com.el.yello.presentation.main.profile.detail.ProfileDetailActivity.Companion.RESULT_PROFILE_MODIFY_FAILURE
import com.el.yello.presentation.main.profile.univmod.UnivProfileModActivity
import com.el.yello.presentation.main.profile.univmod.UnivProfileModViewModel.Companion.TEXT_NONE
import com.el.yello.util.extension.yelloSnackbar
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.base.BindingActivity
import com.example.ui.extension.drawableOf
import com.example.ui.extension.navigateTo
import com.example.ui.extension.setOnSingleClickListener
import com.example.ui.state.UiState
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
    private var schoolModDialog: SchoolModDialog? = null

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
                    schoolModDialog = SchoolModDialog()
                    schoolModDialog?.show(supportFragmentManager, DIALOG_MOD)
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
            if (!result) {
                yelloSnackbar(
                    binding.root,
                    getString(R.string.internet_connection_error_msg),
                )
            }
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

                is UiState.Failure -> yelloSnackbar(
                    binding.root,
                    getString(R.string.internet_connection_error_msg),
                )

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeGetSchoolGroupIdResult() {
        viewModel.getSchoolGroupIdResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) {
                yelloSnackbar(
                    binding.root,
                    getString(R.string.internet_connection_error_msg),
                )
            }
        }.launchIn(lifecycleScope)
    }

    private fun observePostNewProfileResult() {
        viewModel.postToModProfileResult.flowWithLifecycle(lifecycle).onEach { isModified ->
            if (isModified) {
                AmplitudeManager.trackEventWithProperties(EVENT_COMPLETE_PROFILE_CHANGE)
                Intent(this, ProfileDetailActivity::class.java).apply {
                    setResult(Activity.RESULT_OK, this)
                    if (!isFinishing) finish()
                }
            } else {
                Intent(this, ProfileDetailActivity::class.java).apply {
                    setResult(RESULT_PROFILE_MODIFY_FAILURE, this)
                    if (!isFinishing) finish()
                }
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
        if (schoolModDialog != null) schoolModDialog?.dismiss()
    }

    private companion object {
        const val DIALOG_SCHOOL = "school"
        const val DIALOG_GRADE = "grade"
        const val DIALOG_CLASSROOM = "classroom"
        const val DIALOG_MOD = "mod"
        private const val EVENT_COMPLETE_PROFILE_CHANGE = "complete_profile_change"
    }
}
