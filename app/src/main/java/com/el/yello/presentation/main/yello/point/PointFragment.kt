package com.el.yello.presentation.main.yello.point

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.BuildConfig
import com.el.yello.R
import com.el.yello.databinding.FragmentPointBinding
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.vote.VoteViewModel
import com.el.yello.util.extension.yelloSnackbar
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.base.BindingFragment
import com.example.ui.extension.setOnSingleClickListener
import com.example.ui.extension.toast
import com.example.ui.state.UiState
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject

class PointFragment : BindingFragment<FragmentPointBinding>(R.layout.fragment_point) {

    private val yelloViewModel by activityViewModels<YelloViewModel>()
    private val voteViewModel by activityViewModels<VoteViewModel>()

    private var rewardedAd: RewardedAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = voteViewModel
        yelloViewModel.getPurchaseInfoFromServer()
        setConfirmBtnClickListener()
        observeCheckIsSubscribed()
        initAdBtnListener()
        observePostRewardAdState()
    }

    private fun setConfirmBtnClickListener() {
        binding.btnPointConfirmDouble.setOnSingleClickListener {
            requireActivity().finish()
        }
        binding.btnPointJustConfirm.setOnSingleClickListener {
            requireActivity().finish()
        }
    }

    private fun observeCheckIsSubscribed() {
        yelloViewModel.getPurchaseInfoState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    showDoubleConfirm(state.data.isSubscribe)
                    binding.tvPointPlusLabel.isVisible = state.data.isSubscribe
                }

                is UiState.Failure -> showDoubleConfirm(false)

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }

            with(binding) {
                tvPointVotePoint.isVisible = true
                tvPointVotePointPlus.isVisible = true
                tvPointVotePointLabel.isVisible = true
            }
        }.launchIn(lifecycleScope)
    }

    private fun showDoubleConfirm(isDouble: Boolean) {
        with(binding) {
            btnPointJustConfirm.isVisible = !isDouble
            btnPointAdToDouble.isVisible = !isDouble
            btnPointConfirmDouble.isVisible = isDouble
        }
        if (isDouble) {
            binding.tvPointDescription.text = getString(R.string.point_description_double)
            binding.tvPointVotePoint.text = voteViewModel.votePointSum.times(2).toString()
        }
    }

    private fun initAdBtnListener() {
        binding.btnPointAdToDouble.setOnSingleClickListener {
            AmplitudeManager.trackEventWithProperties(
                EVENT_CLICK_ADSENSE,
                JSONObject().put(NAME_ADSENSE_VIEW, VALUE_MESSAGE),
            )
            startLoadingScreen()
            loadRewardAd()
        }
    }

    private fun loadRewardAd() {
        voteViewModel.setUuid()
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(requireContext(),
            BuildConfig.ADMOB_REWARD_KEY, adRequest, object : RewardedAdLoadCallback() {

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    rewardedAd?.apply {
                        setSSV()
                        setRewardAdFinishCallback()
                    }
                    stopLoadingScreen()

                    rewardedAd?.show(requireActivity()) {}
                        ?: yelloSnackbar(binding.root, getString(R.string.pay_ad_fail_to_load))
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    yelloSnackbar(binding.root, getString(R.string.pay_ad_fail_to_load))
                    stopLoadingScreen()
                }
            },
        )
    }

    private fun RewardedAd.setSSV() {
        setServerSideVerificationOptions(
            ServerSideVerificationOptions.Builder()
                .setCustomData(voteViewModel.idempotencyKey.toString())
                .build(),
        )
    }

    private fun RewardedAd.setRewardAdFinishCallback() {
        fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                voteViewModel.postRewardAdToCheck()
                startLoadingScreen()
            }
        }
    }

    private fun observePostRewardAdState() {
        voteViewModel.postRewardAdState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    AmplitudeManager.trackEventWithProperties(
                        EVENT_COMPLETE_ADSENSE,
                        JSONObject().put(NAME_ADSENSE_VIEW, VALUE_MESSAGE),
                    )
                    stopLoadingScreen()
                    showDoubleConfirm(true)
                    val originalPoint = voteViewModel.totalPoint
                    val doubledPoint = originalPoint + voteViewModel.votePointSum
                    with(binding) {
                        tvPointMyPoint.text = doubledPoint.toString()
                        tvPointCurrentPoint.text = doubledPoint.toString()
                    }
                }

                is UiState.Failure -> {
                    stopLoadingScreen()
                    toast(getString(R.string.internet_connection_error_msg))
                }

                is UiState.Loading -> startLoadingScreen()

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun startLoadingScreen() {
        binding.layoutAdCheckLoading.isVisible = true
        requireActivity().window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        )
    }

    private fun stopLoadingScreen() {
        binding.layoutAdCheckLoading.isVisible = false
        requireActivity().window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onDestroy() {
        super.onDestroy()
        rewardedAd = null
    }

    companion object {
        private const val EVENT_CLICK_ADSENSE = "click_adsense"
        private const val EVENT_COMPLETE_ADSENSE = "complete_adsense"
        private const val NAME_ADSENSE_VIEW = "adsense_view"
        private const val VALUE_MESSAGE = "vote"

        @JvmStatic
        fun newInstance() = PointFragment()
    }
}
