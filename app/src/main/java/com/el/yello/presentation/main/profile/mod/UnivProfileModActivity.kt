package com.el.yello.presentation.main.profile.mod

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileUnivModBinding
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
class UnivProfileModActivity :
    BindingActivity<ActivityProfileUnivModBinding>(R.layout.activity_profile_univ_mod) {

    private val viewModel by viewModels<UnivProfileModViewModel>()

    private var univModSearchBottomSheet: UnivModSearchBottomSheet? = null
    private var univModSelectBottomSheet: UnivModSelectBottomSheet? = null

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
                    viewModel.postNewProfileToServer()
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

    private fun observePostNewProfileResult() {
        viewModel.postToModProfileResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (result) {
                toast(getString(R.string.profile_mod_success))
                finish()
            } else {
                toast(getString(R.string.internet_connection_error_msg))
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
    }

    private companion object {
        const val DIALOG_SCHOOL = "school"
        const val DIALOG_SUBGROUP = "subgroup"
        const val DIALOG_YEAR = "year"
    }
}