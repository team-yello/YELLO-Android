package com.yello.presentation.main.profile.manage

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.user.UserApiClient
import com.yello.R
import com.yello.databinding.FragmentProfileQuitDialogBinding
import com.yello.presentation.main.profile.ProfileViewModel
import com.yello.util.context.yelloSnackbar
import timber.log.Timber


class ProfileQuitDialog :
    BindingDialogFragment<FragmentProfileQuitDialogBinding>(R.layout.fragment_profile_quit_dialog) {

    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        setDialogBackground()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initQuitButtonListener()
        initRejectButtonListener()
        observeUserDeleteState()
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 16) * 2

        dialog?.window?.apply {
            setLayout(
                (deviceWidth - dialogHorizontalMargin * 2).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(true)
    }

    private fun initQuitButtonListener() {
        binding.btnProfileDialogQuit.setOnSingleClickListener {
            unlinkYelloAccount()
        }
    }

    private fun initRejectButtonListener() {
        binding.btnProfileDialogReject.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun unlinkYelloAccount() {
        viewModel.deleteUserDataToServer()
    }

    private fun observeUserDeleteState() {
        viewModel.deleteUserState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    unlinkKakaoAccount()
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), "친구 추가 서버 통신 실패")
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    private fun unlinkKakaoAccount() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Timber.d("카카오 로그아웃 실패")
            } else {
                restartApp(requireContext())
            }
        }
    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}