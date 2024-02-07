package com.el.yello.presentation.event

import android.animation.Animator
import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityEventBinding
import com.el.yello.presentation.event.reward.RewardDialog
import com.el.yello.presentation.main.MainActivity.Companion.EXTRA_EVENT
import com.el.yello.presentation.main.MainActivity.Companion.EXTRA_IDEMPOTENCY_KEY
import com.el.yello.presentation.main.MainActivity.Companion.EXTRA_REWARD_LIST
import com.el.yello.presentation.main.ParcelableEvent
import com.el.yello.presentation.main.ParcelableReward
import com.example.ui.base.BindingActivity
import com.example.ui.intent.getCompatibleParcelableExtra
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventActivity : BindingActivity<ActivityEventBinding>(R.layout.activity_event) {
    private val viewModel by viewModels<EventViewModel>()

    private var rewardAdapter: RewardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRewardAdapter()
        getEventExtra()
    }

    private fun getEventExtra() {
        val event = intent.getCompatibleParcelableExtra<ParcelableEvent>(EXTRA_EVENT) ?: return
        with(event) {
            binding.tvEventTitle.text = title
            binding.tvEventSubtitle.text = subTitle

            initEventLottieClickListener()
        }

        rewardAdapter?.submitList(
            intent.getParcelableArrayListExtra<ParcelableReward>(
                EXTRA_REWARD_LIST,
            )?.toList(),
        )

        viewModel.setIdempotencyKey(intent.getStringExtra(EXTRA_IDEMPOTENCY_KEY) ?: return)
    }

    private fun initRewardAdapter() {
        rewardAdapter = RewardAdapter()
        binding.rvEventRewardItem.adapter = rewardAdapter
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
                        startRewardDialog()
                    }

                    override fun onAnimationCancel(p0: Animator) {}

                    override fun onAnimationRepeat(p0: Animator) {}
                })
            }
        }
    }

    private fun startRewardDialog() {
        RewardDialog.newInstance().show(supportFragmentManager, TAG_DIALOG_REWARD)
    }

    companion object {
        private const val TAG_DIALOG_REWARD = "DIALOG_REWARD"
    }
}
