package com.el.yello.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.fragment.app.replace
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityMainBinding
import com.el.yello.presentation.main.look.LookFragment
import com.el.yello.presentation.main.myyello.MyYelloFragment
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.presentation.main.profile.info.ProfileFragment
import com.el.yello.presentation.main.recommend.RecommendFragment
import com.el.yello.presentation.main.yello.YelloFragment
import com.el.yello.presentation.pay.PayResubsNoticeDialog
import com.el.yello.presentation.util.dp
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.intent.stringExtra
import com.example.ui.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    val viewModel by viewModels<ProfileViewModel>()

    val path by stringExtra()
    val type by stringExtra()

    private var backPressedTime: Long = 0

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= BACK_PRESSED_INTERVAL) {
                backPressedTime = System.currentTimeMillis()
                toast(getString(R.string.main_toast_back_pressed))
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUserSubsInfoStateFromServer()
        observeSubsNeededState()
        initBnvItemIconTintList()
        initBnvItemSelectedListener()
        initBnvItemReselectedListener()
        initDeviceToken()
        pushNotificationEvent()
        viewModel.getVoteCount()
        viewModel.setIsFirstLoginData()
        observe()
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun observeSubsNeededState() {
        viewModel.getUserSubsInfoState.onEach { state ->
            when (state) {
                is UiState.Success -> {
                    // TODO : normal -> canceled
                    if (state.data?.subscribe.toString() == "normal") {
                        val expiredDateString = state.data?.expiredDate.toString()
                        val payResubsNoticeFragment =
                            PayResubsNoticeDialog.newInstance(expiredDateString)
                        supportFragmentManager.commitNow {
                            add(
                                payResubsNoticeFragment,
                                "PayResubsNoticeDialog",
                            )
                        }
                        payResubsNoticeFragment.setExpiredDate(expiredDateString)
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun initBnvItemIconTintList() {
        binding.bnvMain.itemIconTintList = null
        binding.bnvMain.selectedItemId = R.id.menu_yello
    }

    private fun initBnvItemSelectedListener() {
        supportFragmentManager.findFragmentById(R.id.fcv_main) ?: navigateTo<YelloFragment>()

        binding.bnvMain.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_recommend -> {
                    AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_RECOMMEND_NAVIGATION)
                    navigateTo<RecommendFragment>()
                }

                R.id.menu_look -> navigateTo<LookFragment>()
                R.id.menu_yello -> {
                    navigateTo<YelloFragment>()
                    binding.btnMainYelloActive.visibility = View.VISIBLE
                    return@setOnItemSelectedListener true
                }

                R.id.menu_my_yello -> navigateTo<MyYelloFragment>()
                R.id.menu_profile -> navigateTo<ProfileFragment>()
            }
            binding.btnMainYelloActive.visibility = View.INVISIBLE
            true
        }
    }

    private fun initBnvItemReselectedListener() {
        binding.bnvMain.setOnItemReselectedListener { menu ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fcv_main)
            when (menu.itemId) {
                R.id.menu_recommend -> {
                    if (currentFragment is RecommendFragment) {
                        currentFragment.scrollToTop()
                    }
                }

                R.id.menu_look -> {
                    if (currentFragment is LookFragment) {
                        currentFragment.scrollToTop()
                    }
                }

                R.id.menu_my_yello -> {
                    if (currentFragment is MyYelloFragment) {
                        currentFragment.scrollToTop()
                    }
                }

                R.id.menu_profile -> {
                    if (currentFragment is ProfileFragment) {
                        currentFragment.scrollToTop()
                    }
                }
            }
        }
    }

    private fun initDeviceToken() {
        viewModel.putDeviceToken()
    }

    private fun initBadge(voteCount: Int) {
        val badgeDrawable = binding.bnvMain.getOrCreateBadge(R.id.menu_my_yello)
        badgeDrawable.verticalOffset = 12.dp
        badgeDrawable.horizontalOffset = 10.dp
        badgeDrawable.number = voteCount
        badgeDrawable.backgroundColor = ContextCompat.getColor(
            this,
            R.color.semantic_red_500,
        )
        badgeDrawable.badgeTextColor = ContextCompat.getColor(
            this,
            R.color.white,
        )
    }

    private fun pushNotificationEvent() {
        when {
            type.equals(NEW_VOTE) -> {
                binding.bnvMain.menu.getItem(3).isChecked = true
                navigateTo<MyYelloFragment>()

                path?.let {
                    val questionId = it.split("/").lastOrNull()?.toLong()
                    questionId?.let {
                        startActivity(MyYelloReadActivity.getIntent(this, questionId))
                    }
                }
            }

            type.equals(NEW_FRIEND) -> {
                binding.bnvMain.menu.getItem(4).isChecked = true
                navigateTo<ProfileFragment>()
            }

            type.equals(VOTE_AVAILABLE) || type.equals(RECOMMEND) -> {
                binding.bnvMain.menu.getItem(2).isChecked = true
                navigateTo<YelloFragment>()
            }
        }
    }

    private fun observe() {
        viewModel.voteCount.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        if (it.data.totalCount != 0) {
                            initBadge(it.data.totalCount)
                        }
                    }

                    is UiState.Failure -> {
                        yelloSnackbar(binding.root, it.msg)
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }

    fun setBadgeCount(count: Int) {
        val badgeDrawable = binding.bnvMain.getOrCreateBadge(R.id.menu_my_yello)
        badgeDrawable.number = count
        badgeDrawable.isVisible = count != 0
    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main, T::class.java.canonicalName)
        }
    }

    companion object {
        const val NEW_VOTE = "NEW_VOTE"
        const val NEW_FRIEND = "NEW_FRIEND"
        const val VOTE_AVAILABLE = "VOTE_AVAILABLE"
        const val RECOMMEND = "RECOMMEND"

        const val BACK_PRESSED_INTERVAL = 2000

        private const val EVENT_CLICK_RECOMMEND_NAVIGATION = "click_recommend_navigation"

        fun getIntent(context: Context, type: String? = null, path: String? = null) =
            Intent(context, MainActivity::class.java).apply {
                putExtra("type", type)
                putExtra("path", path)
            }
    }
}
