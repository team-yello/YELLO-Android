package com.el.yello.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivitySignInBinding
import com.el.yello.presentation.auth.SignInViewModel.Companion.FRIEND_LIST
import com.el.yello.presentation.main.MainActivity
import com.el.yello.presentation.onboarding.activity.EditNameActivity
import com.el.yello.presentation.onboarding.activity.GetAlarmActivity
import com.el.yello.presentation.onboarding.fragment.checkName.CheckNameDialog
import com.el.yello.presentation.tutorial.TutorialAActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignInActivity : BindingActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val viewModel by viewModels<SignInViewModel>()

    private var checkNameDialog: CheckNameDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSignInBtnListener()
        viewModel.getDeviceToken()
        observeDeviceTokenError()
        observeAppLoginError()
        observeKakaoUserInfoResult()
        observeFriendsListValidState()
        observeChangeTokenResult()
        observeUserDataState()
    }

    private fun initSignInBtnListener() {
        binding.btnSignIn.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_onboarding_kakao")
            viewModel.startLogInWithKakao(this)
        }
    }

    private fun observeDeviceTokenError() {
        viewModel.getDeviceTokenError.flowWithLifecycle(lifecycle).onEach { error ->
            if (error) toast(getString(R.string.sign_in_error_connection))
        }.launchIn(lifecycleScope)
    }

    private fun observeAppLoginError() {
        viewModel.isAppLoginAvailable.flowWithLifecycle(lifecycle).onEach { available ->
            if (!available) viewModel.startLogInWithKakao(this)
        }.launchIn(lifecycleScope)
    }

    private fun observeChangeTokenResult() {
        viewModel.postChangeTokenResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) toast(getString(R.string.sign_in_error_connection))
        }.launchIn(lifecycleScope)
    }

    private fun observeKakaoUserInfoResult() {
        viewModel.getKakaoInfoResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) yelloSnackbar(binding.root, getString(R.string.msg_error))
        }.launchIn(lifecycleScope)
    }

    // ChangeToken Failure -> 카카오에 등록된 유저 정보 받아온 후 친구목록 동의 or 온보딩 화면으로 이동
    private fun observeFriendsListValidState() {
        viewModel.getKakaoValidState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    val friendScope = state.data.find { it.id == FRIEND_LIST }
                    if (friendScope?.agreed == true) {
                        startCheckNameDialog()
                    } else {
                        startSocialSyncActivity()
                    }
                }

                is UiState.Failure -> yelloSnackbar(binding.root, getString(R.string.msg_error))

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    // ChangeToken Success -> 서버에 등록된 유저 정보가 있는지 확인 후 메인 액티비티로 이동
    private fun observeUserDataState() {
        viewModel.getUserProfileState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (viewModel.getIsFirstLoginData()) {
                        if (viewModel.isResigned) {
                            startActivity(TutorialAActivity.newIntent(this, false))
                        } else {
                            startMainActivity()
                        }
                    } else {
                        startActivity(Intent(this, GetAlarmActivity::class.java))
                    }
                }

                is UiState.Failure -> yelloSnackbar(binding.root, getString(R.string.msg_error))

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    private fun startSocialSyncActivity() {
        Intent(this, SocialSyncActivity::class.java).apply {
            addPutExtra()
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    private fun startCheckNameDialog() {
        val bundle = Bundle().apply { addPutExtra() }
        if (viewModel.isUserNameBlank()) {
            Intent(SignInActivity(), EditNameActivity::class.java).apply {
                putExtras(bundle)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
            finish()
        } else {
            checkNameDialog = CheckNameDialog()
            binding.btnSignIn.visibility = View.GONE
            binding.ivSignIn.visibility = View.GONE
            binding.ivSignInKakao.visibility = View.GONE
            binding.tvSignInTitle.visibility = View.GONE
            binding.tvSignInSubtitle.visibility = View.GONE
            checkNameDialog?.arguments = bundle
            checkNameDialog?.show(supportFragmentManager, CHECK_NAME_DIALOG)
        }
    }

    private fun Intent.addPutExtra() {
        if (viewModel.checkKakaoUserInfoStored()) {
            putExtra(EXTRA_KAKAO_ID, viewModel.kakaoUserInfo.id)
            putExtra(EXTRA_NAME, viewModel.kakaoUserInfo.kakaoAccount?.name.orEmpty())
            putExtra(EXTRA_GENDER, viewModel.kakaoUserInfo.kakaoAccount?.gender.toString())
            putExtra(EXTRA_EMAIL, viewModel.kakaoUserInfo.kakaoAccount?.email.orEmpty())
            putExtra(
                EXTRA_PROFILE_IMAGE,
                viewModel.kakaoUserInfo.kakaoAccount?.profile?.profileImageUrl.orEmpty()
            )
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    private fun Bundle.addPutExtra() {
        if (viewModel.checkKakaoUserInfoStored()) {
            putLong(EXTRA_KAKAO_ID, viewModel.kakaoUserInfo.id ?: 0)
            putString(EXTRA_NAME, viewModel.kakaoUserInfo.kakaoAccount?.name.orEmpty())
            putString(EXTRA_GENDER, viewModel.kakaoUserInfo.kakaoAccount?.gender.toString())
            putString(EXTRA_EMAIL, viewModel.kakaoUserInfo.kakaoAccount?.email.orEmpty())
            putString(
                EXTRA_PROFILE_IMAGE,
                viewModel.kakaoUserInfo.kakaoAccount?.profile?.profileImageUrl.orEmpty()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        checkNameDialog?.dismiss()
    }

    companion object {
        const val EXTRA_KAKAO_ID = "KAKAO_ID"
        const val EXTRA_EMAIL = "KAKAO_EMAIL"
        const val EXTRA_PROFILE_IMAGE = "PROFILE_IMAGE"
        const val EXTRA_NAME = "NAME"
        const val EXTRA_GENDER = "GENDER"
        const val CHECK_NAME_DIALOG = "CHECK_NAME_DIALOG"
    }
}
