package com.el.yello.presentation.setting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitReasonBinding
import com.example.ui.base.BindingActivity
import com.example.ui.context.colorOf
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileQuitReasonActivity :
    BindingActivity<ActivityProfileQuitReasonBinding>(R.layout.activity_profile_quit_reason) {

    private val viewModel by viewModels<SettingViewModel>()

    private var _adapter: ProfileQuitReasonAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private var clickedItemPosition: Int = RecyclerView.NO_POSITION
    private var isItemClicked: Boolean = false
    private var profileQuitDialog: ProfileQuitDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        initQuitReasonAdapter()
        initBackBtnListener()
        initInviteDialogBtnListener()
        setQuitReasonList()
    }

    private fun initQuitReasonAdapter() {
        _adapter = ProfileQuitReasonAdapter(
            storeQuitReason = ::storeQuitReason,
            setEtcText = ::setEtcText,
            onItemClickListener = ::setClickedItemUI
        )
        binding.rvQuitReason.adapter = adapter
    }

    private fun storeQuitReason(reason: String) {
        viewModel.quitReasonText = reason
    }

    private fun setEtcText(etc: String) {
        viewModel.etcText.value = etc
    }

    private fun setClickedItemUI(position: Int, isClicked: Boolean) {
        clickedItemPosition = position
        isItemClicked = isClicked
        if (isItemClicked) {
            if (clickedItemPosition == 7) {
                nonClickedButtonUI()
            } else {
                clickedButtonUI()
            }
        }
    }

    private fun initInviteDialogBtnListener() {
        binding.btnProfileQuitReasonDone.setOnSingleClickListener {
            profileQuitDialog = ProfileQuitDialog()
            profileQuitDialog?.show(supportFragmentManager, QUIT_DIALOG)
        }
    }

    private fun initBackBtnListener() {
        binding.btnProfileQuitReasonBack.setOnSingleClickListener { finish() }
    }

    private fun setQuitReasonList() {
        adapter.submitList(resources.getStringArray(R.array.quit_reasons).toList())
    }

    private fun clickedButtonUI() {
        with(binding.btnProfileQuitReasonDone) {
            setBackgroundResource(R.drawable.shape_black_fill_grayscales700_line_100_rect)
            setTextColor(colorOf(R.color.semantic_red_500))
            isEnabled = true
        }
    }

    private fun nonClickedButtonUI() {
        with(binding.btnProfileQuitReasonDone) {
            setBackgroundResource(R.drawable.shape_black_fill_grayscales600_line_100_rect)
            setTextColor(colorOf(R.color.grayscales_600))
            isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        profileQuitDialog?.dismiss()
        _adapter = null
    }

    private companion object {
        const val QUIT_DIALOG = "quitDialog"
    }
}
