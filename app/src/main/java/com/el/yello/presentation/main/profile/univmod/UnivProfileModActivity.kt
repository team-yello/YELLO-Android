package com.el.yello.presentation.main.profile.univmod

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileUnivModBinding
import com.el.yello.presentation.main.profile.detail.ProfileDetailActivity
import com.el.yello.presentation.main.profile.detail.ProfileDetailActivity.Companion.RESULT_PROFILE_MODIFY_FAILURE
import com.el.yello.presentation.main.profile.schoolmod.SchoolProfileModActivity
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
class UnivProfileModActivity :
    BindingActivity<ActivityProfileUnivModBinding>(R.layout.activity_profile_univ_mod) {

    private val viewModel by viewModels<UnivProfileModViewModel>()

    private var univModSearchBottomSheet: UnivModSearchBottomSheet? = null
    private var univModSelectBottomSheet: UnivModSelectBottomSheet? = null
    private var univModDialog: UnivModDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUserData()
        initChangeToSchoolBtnListener()
        initSaveBtnListener()
        initBackBtnListener()
        initBottomSheetListener()
        observeGetUserDataResult()
        observeGetIsModValidResult()
        observePostNewProfileResult()
    }

    private fun initUserData() {
        binding.vm = viewModel
        viewModel.getUserDataFromServer()
        viewModel.getIsModValidFromServer()
    }

    private fun initChangeToSchoolBtnListener() {
        binding.btnChangeUnivToSchool.setOnSingleClickListener {
            navigateTo<SchoolProfileModActivity>()
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

                viewModel.subGroup.value == TEXT_NONE -> {
                    binding.layoutProfileModSubgroup.background =
                        drawableOf(R.drawable.shape_red_fill_red500_line_10_rect)
                    binding.tvProfileModSubgroupError.isVisible = true
                }

                else -> {
                    univModDialog = UnivModDialog()
                    univModDialog?.show(supportFragmentManager, DIALOG_MOD)
                }
            }
        }
    }

    private fun initBackBtnListener() {
        binding.btnProfileModBack.setOnSingleClickListener { finish() }
    }

    private fun initBottomSheetListener() {
        binding.btnSearchSchool.setOnSingleClickListener {
            univModSearchBottomSheet = UnivModSearchBottomSheet.newInstance(true)
            univModSearchBottomSheet?.show(supportFragmentManager, DIALOG_SCHOOL)
        }
        binding.btnSearchSubgroup.setOnSingleClickListener {
            univModSearchBottomSheet = UnivModSearchBottomSheet.newInstance(false)
            univModSearchBottomSheet?.show(supportFragmentManager, DIALOG_SUBGROUP)
        }
        binding.btnSearchYear.setOnSingleClickListener {
            univModSelectBottomSheet = UnivModSelectBottomSheet.newInstance()
            univModSelectBottomSheet?.show(supportFragmentManager, DIALOG_YEAR)
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
            viewModel.resetStateVariables()
        }.launchIn(lifecycleScope)
    }

    fun checkIsTextNone() {
        if (viewModel.subGroup.value != TEXT_NONE) {
            binding.layoutProfileModSubgroup.background =
                drawableOf(R.drawable.shape_grayscales900_fill_12_rect)
            binding.tvProfileModSubgroupError.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (univModSearchBottomSheet != null) univModSearchBottomSheet?.dismiss()
        if (univModSelectBottomSheet != null) univModSelectBottomSheet?.dismiss()
        if (univModDialog != null) univModDialog?.dismiss()
    }

    private companion object {
        const val DIALOG_SCHOOL = "school"
        const val DIALOG_SUBGROUP = "subgroup"
        const val DIALOG_YEAR = "year"
        const val DIALOG_MOD = "mod"
        private const val EVENT_COMPLETE_PROFILE_CHANGE = "complete_profile_change"
    }
}
