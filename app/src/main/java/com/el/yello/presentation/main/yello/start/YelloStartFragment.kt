package com.el.yello.presentation.main.yello.start

import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloStartBinding
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.vote.VoteActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.type.YelloState
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = requireActivity().windowManager.currentWindowMetrics
                size.x = windowMetrics.bounds.width()
                size.y = windowMetrics.bounds.height()
            } else {
                display.getRealSize(size)
            }
            val displayWidth = size.x
            val displayHeight = size.y

            layoutParams.width = (2.22 * displayWidth).toInt()
            setMargins(this, 0, 0, 0, (-0.435 * displayHeight).toInt())
        }
    }

    private fun initShadowView() {
        binding.shadowStart.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
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
        viewModel.getPurchaseInfoState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data?.isSubscribe == true) {
                        binding.layoutSubsDouble.visibility = View.VISIBLE
                    } else {
                        binding.layoutSubsDouble.visibility = View.GONE
                    }
                }

                is UiState.Failure -> {
                    binding.layoutSubsDouble.visibility = View.GONE
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    companion object {
        private const val EVENT_CLICK_VOTE_START = "click_vote_start"

        @JvmStatic
        fun newInstance() = YelloStartFragment()
    }
}
