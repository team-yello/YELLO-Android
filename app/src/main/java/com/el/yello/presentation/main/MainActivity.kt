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
import androidx.fragment.app.replace
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityMainBinding
import com.el.yello.presentation.event.EventActivity
import com.el.yello.presentation.main.MainViewModel.Companion.CODE_UNAVAILABLE_EVENT
import com.el.yello.presentation.main.dialog.notice.NoticeDialog
import com.el.yello.presentation.main.look.LookFragment
import com.el.yello.presentation.main.myyello.MyYelloFragment
import com.el.yello.presentation.main.myyello.read.MyYelloReadActivity
import com.el.yello.presentation.main.profile.info.ProfileFragment
import com.el.yello.presentation.main.recommend.RecommendFragment
import com.el.yello.presentation.main.yello.YelloFragment
import com.el.yello.presentation.pay.dialog.PayReSubsNoticeDialog
import com.el.yello.presentation.util.dp
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.entity.event.Event
import com.example.domain.enum.SubscribeType.CANCELED
import com.example.ui.base.BindingActivity
import com.example.ui.extension.stringExtra
import com.example.ui.state.UiState.Empty
import com.example.ui.state.UiState.Failure
import com.example.ui.state.UiState.Loading
import com.example.ui.state.UiState.Success
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<MainViewModel>()

    private val path by stringExtra()
    private val type by stringExtra()

    private var backPressedTime: Long = 0
    private var userSubsStateJob: Job? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= BACK_PRESSED_INTERVAL) {
                backPressedTime = System.currentTimeMillis()
                yelloSnackbar(binding.root, getString(R.string.main_toast_back_pressed))
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackPressedCallback()
        initBnvItemIconTintList()
        initBnvItemSelectedListener()
        initBnvItemReselectedListener()
        initPushNotificationEvent()
        setupGetUserSubsState()
        setupGetNoticeState()
        setupGetVoteCountState()
        setupGetEventState()
    }

    private fun initBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun initBnvItemIconTintList() {
        binding.bnvMain.itemIconTintList = null
        binding.bnvMain.selectedItemId = R.id.menu_yello
    }

    private fun initBnvItemSelectedListener() {
        supportFragmentManager.findFragmentById(R.id.fcv_main)
            ?: navigateTo<YelloFragment>()

        binding.bnvMain.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_recommend -> {
                    AmplitudeUtils.trackEventWithProperties(
                        EVENT_CLICK_RECOMMEND_NAVIGATION,
                    )
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

    private fun initPushNotificationEvent() {
        when (type) {
            PUSH_TYPE_NEW_VOTE -> {
                binding.bnvMain.menu.getItem(3).isChecked = true
                navigateTo<MyYelloFragment>()

                path?.let {
                    val questionId = it.split("/").lastOrNull()?.toLong()
                    questionId?.let {
                        startActivity(MyYelloReadActivity.getIntent(this, questionId))
                    }
                }
            }

            PUSH_TYPE_NEW_FRIEND -> {
                binding.bnvMain.menu.getItem(4).isChecked = true
                navigateTo<ProfileFragment>()
            }

            PUSH_TYPE_VOTE_AVAILABLE, PUSH_TYPE_RECOMMEND -> {
                binding.bnvMain.menu.getItem(2).isChecked = true
                navigateTo<YelloFragment>()
            }
        }
    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main, T::class.java.canonicalName)
        }
    }

    private fun setupGetUserSubsState() {
        userSubsStateJob = viewModel.getUserSubsState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is Empty -> return@onEach
                    is Loading -> return@onEach
                    is Success -> {
                        if (state.data?.subscribe != CANCELED) return@onEach
                        // TODO : 도메인 모델에 변환된 날짜로 파싱되도록 보완
                        val expiredDateString = state.data?.expiredDate.toString()
                        val expiredDate =
                            SimpleDateFormat(EXPIRED_DATE_FORMAT).parse(
                                expiredDateString,
                            )
                                ?: return@onEach
                        val currentDate = Calendar.getInstance().time
                        val daysDifference = TimeUnit.DAYS.convert(
                            expiredDate.time - currentDate.time,
                            TimeUnit.MILLISECONDS,
                        )
                        if (daysDifference <= 1) {
                            PayReSubsNoticeDialog.newInstance(expiredDateString)
                                .show(supportFragmentManager, PAY_RESUBS_DIALOG)
                        }
                    }

                    is Failure -> yelloSnackbar(
                        binding.root,
                        getString(R.string.internet_connection_error_msg),
                    )
                }
            }.launchIn(lifecycleScope)
    }

    private fun setupGetNoticeState() {
        viewModel.getNoticeState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is Empty -> yelloSnackbar(
                        binding.root,
                        getString(R.string.internet_connection_error_msg),
                    )

                    is Loading -> return@onEach
                    is Success -> {
                        if (!state.data.isAvailable) return@onEach
                        NoticeDialog.newInstance(
                            imageUrl = state.data.imageUrl,
                            redirectUrl = state.data.redirectUrl,
                        ).show(supportFragmentManager, TAG_NOTICE_DIALOG)
                    }

                    is Failure -> yelloSnackbar(
                        binding.root,
                        getString(R.string.internet_connection_error_msg),
                    )
                }
            }.launchIn(lifecycleScope)
    }

    private fun setupGetVoteCountState() {
        viewModel.voteCount.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is Empty -> return@onEach

                    is Loading -> return@onEach

                    is Success -> {
                        if (state.data.totalCount != 0) {
                            initBadge(state.data.totalCount)
                        }
                    }

                    is Failure -> {
                        yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun setupGetEventState() {
        viewModel.getEventState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is Success -> {
                        Intent(this, EventActivity::class.java).apply {
                            putExtra(EXTRA_EVENT, state.data.toParcelableEvent())
                            val rewardList = ArrayList<ParcelableReward>()
                            state.data.rewardList?.map { reward ->
                                rewardList.add(reward.toParcelableReward())
                            }
                            putParcelableArrayListExtra(
                                EXTRA_REWARD_LIST,
                                rewardList,
                            )
                            putExtra(EXTRA_IDEMPOTENCY_KEY, viewModel.idempotencyKey.toString())
                            startActivity(this)
                        }
                    }

                    is Failure -> {
                        when (state.msg) {
                            CODE_UNAVAILABLE_EVENT -> return@onEach
                            else -> yelloSnackbar(
                                binding.root,
                                getString(R.string.internet_connection_error_msg),
                            )
                        }
                    }

                    is Empty -> return@onEach
                    is Loading -> return@onEach
                }
            }.launchIn(lifecycleScope)
    }

    private fun Event.toParcelableEvent() = ParcelableEvent(
        title = title,
        subTitle = subTitle,
        defaultLottieUrl = animationUrlList[0],
        openLottieUrl = animationUrlList[1]
    )

    private fun Event.Reward.toParcelableReward() = ParcelableReward(
        imageUrl = imageUrl,
        name = name,
    )

    fun setBadgeCount(count: Int) {
        val badgeDrawable = binding.bnvMain.getOrCreateBadge(R.id.menu_my_yello)
        badgeDrawable.number = count
        badgeDrawable.isVisible = count != 0
    }

    companion object {
        private const val EXTRA_TYPE = "type"
        private const val EXTRA_PATH = "path"
        const val EXTRA_EVENT = "EVENT"
        const val EXTRA_REWARD_LIST = "REWARD_LIST"
        const val EXTRA_IDEMPOTENCY_KEY = "IDEMPOTENCY_KEY"

        const val PUSH_TYPE_NEW_VOTE = "NEW_VOTE"
        const val PUSH_TYPE_NEW_FRIEND = "NEW_FRIEND"
        const val PUSH_TYPE_VOTE_AVAILABLE = "VOTE_AVAILABLE"
        const val PUSH_TYPE_RECOMMEND = "RECOMMEND"

        const val BACK_PRESSED_INTERVAL = 2000
        const val EXPIRED_DATE_FORMAT = "yyyy-MM-dd"
        const val PAY_RESUBS_DIALOG = "PayResubsNoticeDialog"
        private const val EVENT_CLICK_RECOMMEND_NAVIGATION =
            "click_recommend_navigation"

        private const val TAG_NOTICE_DIALOG = "NOTICE_DIALOG"

        @JvmStatic
        fun getIntent(context: Context, type: String? = null, path: String? = null) =
            Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_TYPE, type)
                putExtra(EXTRA_PATH, path)
            }
    }
}
