package com.el.yello.presentation.main.yello.start

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloStartBinding
import com.el.yello.presentation.main.yello.YelloState
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.vote.VoteActivity
import com.el.yello.util.AmplitudeManager
import com.example.ui.base.BindingFragment
import com.example.ui.extension.drawableOf
import com.example.ui.extension.getCompatibleRealSize
import com.example.ui.extension.dpToPx
import com.example.ui.state.UiState
import com.example.ui.extension.setMargins
import com.example.ui.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class YelloStartFragment :
    BindingFragment<FragmentYelloStartBinding>(R.layout.fragment_yello_start) {
    private val viewModel by activityViewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setBalloonVisibility()
        initEntranceLottie()
        initShadowView()
        initVoteBtnClickListener()
        initVoteBtnTouchListener()
        observeCheckIsSubscribed()
        viewModel.getPurchaseInfoFromServer()
    }

    private fun setBalloonVisibility() {
        val yelloState = viewModel.yelloState.value
        if (yelloState is UiState.Success) {
            if (yelloState.data is YelloState.Valid) {
                binding.layoutStartBalloon.visibility =
                    if ((yelloState.data as YelloState.Valid).hasFourFriends) View.GONE else View.VISIBLE
            }
        }
    }

    private fun initEntranceLottie() {
        with(binding.lottieStartEntrance) {
            val size = Point()
            getCompatibleRealSize(size)
            val displayWidth = size.x
            val displayHeight = size.y

            layoutParams.width = (2.22 * displayWidth).toInt()
            setMargins(0, 0, 0, (-0.435 * displayHeight).toInt())
        }
    }

    private fun initShadowView() {
        binding.shadowStart.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val size = Point()
        getCompatibleRealSize(size)
        val displayWidth = size.x
        binding.layoutSubsDouble.setMargins(
            left = 0,
            top = displayWidth + MARGIN_TOP_SUBSCRIBE_LAYOUT.dpToPx(requireContext()),
            right = 0,
            bottom = 0,
        )
    }

    private fun initVoteBtnClickListener() {
        binding.btnStartVote.setOnSingleClickListener {
            AmplitudeManager.trackEventWithProperties(EVENT_CLICK_VOTE_START)
            intentToVoteScreen()
        }
    }

    // TODO : SuppressLint 제거
    @SuppressLint("ClickableViewAccessibility")
    private fun initVoteBtnTouchListener() {
        binding.btnStartVote.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                ACTION_DOWN -> {
                    with(binding.btnStartVote) {
                        background = drawableOf(R.drawable.shape_yello_main_500_fill_100_rect)
                        setPadding(
                            0,
                            PADDING_HORIZONTAL_VOTE_BTN_PRESSED.dpToPx(requireContext()),
                            0,
                            PADDING_HORIZONTAL_VOTE_BTN_PRESSED.dpToPx(requireContext()),
                        )
                    }
                }

                ACTION_UP -> {
                    with(binding.btnStartVote) {
                        background =
                            drawableOf(R.drawable.shape_yello_main_500_fill_500_botshadow_rect)
                        setPadding(
                            0,
                            PADDING_TOP_VOTE_BTN.dpToPx(requireContext()),
                            0,
                            PADDING_BOTTOM_VOTE_BTN.dpToPx(requireContext()),
                        )
                    }
                }
            }
            false
        }
    }

    private fun intentToVoteScreen() {
        Intent(activity, VoteActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun observeCheckIsSubscribed() {
        viewModel.getPurchaseInfoState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        if (state.data.isSubscribe) {
                            binding.layoutSubsDouble.visibility = View.VISIBLE
                            return@onEach
                        }
                        binding.layoutSubsDouble.visibility = View.GONE
                    }

                    is UiState.Failure -> {
                        binding.layoutSubsDouble.visibility = View.GONE
                    }

                    is UiState.Loading -> {}

                    is UiState.Empty -> {}
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        private const val EVENT_CLICK_VOTE_START = "click_vote_start"

        private const val MARGIN_TOP_SUBSCRIBE_LAYOUT = 16
        private const val PADDING_HORIZONTAL_VOTE_BTN_PRESSED = 21
        private const val PADDING_TOP_VOTE_BTN = 19
        private const val PADDING_BOTTOM_VOTE_BTN = 23

        @JvmStatic
        fun newInstance() = YelloStartFragment()
    }
}
