package com.el.yello.presentation.main.yello.start

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloStartBinding
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.vote.VoteActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.type.YelloState
import com.example.ui.base.BindingFragment
import com.example.ui.context.setMargins
import com.example.ui.fragment.getCompatibleRealSize
import com.example.ui.view.UiState
import com.example.ui.view.dpToPx
import com.example.ui.view.setOnSingleClickListener
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
        observeCheckIsSubscribed()
        viewModel.getPurchaseInfoFromServer()
    }

    private fun setBalloonVisibility() {
        // TODO: 삭제 이후 바로 반영되도록 로직 보완
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
            setMargins(this, 0, 0, 0, (-0.435 * displayHeight).toInt())
        }
    }

    private fun initShadowView() {
        binding.shadowStart.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val size = Point()
        getCompatibleRealSize(size)
        val displayWidth = size.x
        setMargins(
            binding.layoutSubsDouble,
            0,
            displayWidth + MARGIN_SUBSCRIBE_LAYOUT.dpToPx(requireContext()),
            0,
            0,
        )
    }

    private fun initVoteBtnClickListener() {
        binding.btnStartVote.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_VOTE_START)
            intentToVoteScreen()
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

        private const val MARGIN_SUBSCRIBE_LAYOUT = 16

        @JvmStatic
        fun newInstance() = YelloStartFragment()
    }
}
