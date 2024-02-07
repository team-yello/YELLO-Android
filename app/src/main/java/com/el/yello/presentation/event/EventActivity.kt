package com.el.yello.presentation.event

import android.animation.Animator
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityEventBinding
import com.el.yello.presentation.event.reward.RewardDialog
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState.Empty
import com.example.ui.view.UiState.Failure
import com.example.ui.view.UiState.Loading
import com.example.ui.view.UiState.Success
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EventActivity : BindingActivity<ActivityEventBinding>(R.layout.activity_event) {
    private val viewModel by viewModels<EventViewModel>()

    private var rewardAdapter: RewardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRewardAdapter()
        setupGetEventState()
    }

    private fun initRewardAdapter() {
        rewardAdapter = RewardAdapter()
        binding.rvEventRewardItem.adapter = rewardAdapter
    }

    private fun setupGetEventState() {
        viewModel.getEventState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is Success -> {
                        binding.tvEventTitle.text = state.data.title
                        binding.tvEventSubtitle.text = state.data.subTitle

                        initEventLottieClickListener()
                        rewardAdapter?.submitList(state.data.rewardList)
                    }

                    is Failure -> {
                        yelloSnackbar(
                            binding.root,
                            getString(R.string.event_get_event_failure),
                        )
                    }

                    is Empty -> {}
                    is Loading -> {}
                }
            }.launchIn(lifecycleScope)
    }

    private fun initEventLottieClickListener() {
        // TODO: 필요하면 터치 영역 조정
        with(binding.lottieEvent) {
            setOnSingleClickListener {
                setOnClickListener(null)
                setAnimation(R.raw.lottie_event_open)
                loop(false)
                playAnimation()
                addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator, isReverse: Boolean) {
                        super.onAnimationStart(animation, isReverse)
                    }

                    override fun onAnimationStart(p0: Animator) {}

                    override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                        super.onAnimationEnd(animation, isReverse)
                    }

                    override fun onAnimationEnd(p0: Animator) {
                        RewardDialog.newInstance().show(supportFragmentManager, TAG_DIALOG_REWARD)
                    }

                    override fun onAnimationCancel(p0: Animator) {}

                    override fun onAnimationRepeat(p0: Animator) {}
                })
            }
        }
    }

    companion object {
        private const val TAG_DIALOG_REWARD = "DIALOG_REWARD"
    }
}
