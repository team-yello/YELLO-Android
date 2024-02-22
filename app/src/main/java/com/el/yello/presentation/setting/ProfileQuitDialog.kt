package com.el.yello.presentation.setting

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentProfileQuitDialogBinding
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.base.BindingDialogFragment
import com.example.ui.extension.toast
import com.example.ui.state.UiState
import com.example.ui.extension.setOnSingleClickListener
import com.example.ui.util.Utils.restartApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject

@AndroidEntryPoint
class ProfileQuitDialog :
    BindingDialogFragment<FragmentProfileQuitDialogBinding>(R.layout.fragment_profile_quit_dialog) {

    private val viewModel by activityViewModels<SettingViewModel>()

    override fun onStart() {
        super.onStart()
        setDialogBackground()
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 16) * 2

        dialog?.window?.apply {
            setLayout(
                (deviceWidth - dialogHorizontalMargin * 2).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initQuitBtnListener()
        initRejectBtnListener()
        observeUserDeleteState()
        observeKakaoQuitState()
    }

    private fun initRejectBtnListener() {
        binding.btnProfileDialogReject.setOnSingleClickListener { dismiss() }
    }

    private fun initQuitBtnListener() {
        binding.btnProfileDialogQuit.setOnSingleClickListener {
            AmplitudeManager.trackEventWithProperties(
                "click_profile_withdrawal",
                JSONObject().put("withdrawal_button", "withdrawal4"),
            )
            viewModel.deleteUserDataToServer()
        }
    }

    private fun observeUserDeleteState() {
        viewModel.deleteUserState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> viewModel.quitKakaoAccount()

                is UiState.Failure -> toast(getString(R.string.internet_connection_error_msg))

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeKakaoQuitState() {
        viewModel.kakaoQuitState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    AmplitudeManager.trackEventWithProperties("complete_withdrawal")
                    restartApp(requireContext(),null)
                }

                is UiState.Failure -> toast(getString(R.string.internet_connection_error_msg))

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }
}
