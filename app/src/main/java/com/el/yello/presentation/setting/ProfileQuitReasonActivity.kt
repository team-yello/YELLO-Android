package com.el.yello.presentation.setting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitReasonBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.base.BindingActivity
import com.example.ui.context.colorOf
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileQuitReasonActivity :
    BindingActivity<ActivityProfileQuitReasonBinding>(R.layout.activity_profile_quit_reason) {
    private lateinit var quitReasonList: List<String>
    private val viewModel by viewModels<ProfileViewModel>()
    private var clickedItemPosition: Int = RecyclerView.NO_POSITION
    private var isItemClicked: Boolean = false
    private var profileQuitDialog: ProfileQuitDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initQuitReasonAdapter()
        initBackBtnListener()
        initInviteDialogBtnListener()
    }

    private fun initQuitReasonAdapter() {
        viewModel.addQuitReasonList(this)
        quitReasonList = viewModel.quitReasonData.value ?: emptyList()
        val adapter = ProfileQuitReasonAdapter(
            storeQuitReason = ::storeQuitReason,
            setEtcText = ::setEtcText,
        ) { position, clicked ->
            clickedItemPosition = position
            isItemClicked = clicked
            if (isItemClicked) {
                if (clickedItemPosition == 7) {
                    nonClickedButtonUI()
                } else {
                    clickedButtonUI()
                }
            }
        }
        binding.rvQuitReason.adapter = adapter
        adapter.submitList(ArrayList(quitReasonList))
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

    private fun storeQuitReason(reason: String) {
        viewModel.setQuitReason(reason)
    }

    private fun setEtcText(etc: String) {
        viewModel.setEtcText(etc)
    }

    override fun onDestroy() {
        super.onDestroy()
        profileQuitDialog?.dismiss()
    }

    private companion object {
        const val QUIT_DIALOG = "quitDialog"
    }
}
