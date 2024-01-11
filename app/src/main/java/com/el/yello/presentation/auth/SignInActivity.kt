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

    private var userKakaoId: Long = 0
    private var userName: String = String()
    private var userGender: String = String()
    private var userEmail: String = String()
    private var userImage: String = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSignInBtnListener()
        observeDeviceTokenError()
        observeAppLoginError()
        observeKakaoUserInfoState()
        observeFriendsListValidState()
        observePostChangeTokenState()
        observeUserDataState()
    }

    // TODO : UiState 하나로 병합
    private fun initSignInBtnListener() {
        binding.btnSignIn.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_onboarding_kakao")
            viewModel.getKakaoToken(this)
        }
    }

    private fun observePostChangeTokenState() {
        viewModel.postChangeTokenState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    viewModel.getUserDataFromServer()
                }

                is UiState.Failure -> {
                    // TODO : when문으로 수정
                    if (state.msg == CODE_NOT_SIGNED_IN || state.msg == CODE_NO_UUID) {
                        viewModel.getKakaoInfo()
                    } else {
                        toast(getString(R.string.sign_in_error_connection))
                    }
                }

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

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

                is UiState.Empty -> yelloSnackbar(binding.root, getString(R.string.msg_error))

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeDeviceTokenError() {
        viewModel.getDeviceTokenError.flowWithLifecycle(lifecycle).onEach { error ->
            if (error) toast(getString(R.string.sign_in_error_connection))
        }.launchIn(lifecycleScope)
    }

    private fun observeAppLoginError() {
        viewModel.isAppLoginAvailable.flowWithLifecycle(lifecycle).onEach { available ->
            if (!available) viewModel.getKakaoToken(this)
        }.launchIn(lifecycleScope)
    }

    private fun observeKakaoUserInfoState() {
        viewModel.getKakaoInfoState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    userKakaoId = state.data?.id ?: 0
                    userName = state.data?.kakaoAccount?.name.toString()
                    userGender = state.data?.kakaoAccount?.gender.toString()
                    userEmail = state.data?.kakaoAccount?.email.toString()
                    userImage = state.data?.kakaoAccount?.profile?.profileImageUrl.toString()
                    viewModel.checkFriendsListValid()
                }

                is UiState.Failure -> yelloSnackbar(binding.root, getString(R.string.msg_error))

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

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
        if (userName.isBlank() || userName.isEmpty()) {
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
        putExtra(EXTRA_KAKAO_ID, userKakaoId)
        putExtra(EXTRA_NAME, userName)
        putExtra(EXTRA_GENDER, userGender)
        putExtra(EXTRA_EMAIL, userEmail)
        putExtra(EXTRA_PROFILE_IMAGE, userImage)
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    private fun Bundle.addPutExtra() {
        putLong(EXTRA_KAKAO_ID, userKakaoId)
        putString(EXTRA_NAME, userName)
        putString(EXTRA_GENDER, userGender)
        putString(EXTRA_EMAIL, userEmail)
        putString(EXTRA_PROFILE_IMAGE, userImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        checkNameDialog = null
    }

    companion object {
        const val EXTRA_KAKAO_ID = "KAKAO_ID"
        const val EXTRA_EMAIL = "KAKAO_EMAIL"
        const val EXTRA_PROFILE_IMAGE = "PROFILE_IMAGE"
        const val EXTRA_NAME = "NAME"
        const val EXTRA_GENDER = "GENDER"
        const val CODE_NOT_SIGNED_IN = "403"
        const val CODE_NO_UUID = "404"
        const val CHECK_NAME_DIALOG = "CHECK_NAME_DIALOG"
    }
}
