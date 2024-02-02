package com.el.yello.presentation.main.yello.vote

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityVoteBinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.transformation.FadeOutTransformation
import com.example.ui.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject

@AndroidEntryPoint
class VoteActivity : BindingActivity<ActivityVoteBinding>(R.layout.activity_vote) {
    private val viewModel by viewModels<VoteViewModel>()

    private val voteAdapter by lazy {
        VoteAdapter(
            fragmentActivity = this,
            voteListSize = viewModel.totalListCount + 1,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        setupCurrentNoteIndex()
        setupPostVoteState()
        setupVoteState()
    }

    private fun setupVoteState() {
        viewModel.voteState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Loading -> {}
                    is UiState.Success -> initVoteViewPager()
                    is UiState.Empty -> {}
                    is UiState.Failure -> {
                        // TODO: 커스텀 스낵바로 변경
                        toast(getString(R.string.msg_error))
                        finish()
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun initVoteViewPager() {
        with(binding) {
            vpVote.adapter = voteAdapter
            vpVote.setPageTransformer(FadeOutTransformation())
            vpVote.isUserInputEnabled = false
        }
    }

    private fun setupCurrentNoteIndex() {
        viewModel._currentNoteIndex.flowWithLifecycle(lifecycle)
            .onEach { index ->
                if (index <= viewModel.totalListCount) {
                    val properties = JSONObject().put(JSON_VOTE_STEP, index + 1)
                    AmplitudeUtils.trackEventWithProperties(EVENT_VIEW_VOTE_QUESTION, properties)
                }
            }.launchIn(lifecycleScope)
    }

    private fun setupPostVoteState() {
        viewModel.postVoteState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Loading -> {}
                    is UiState.Failure -> {
                        yelloSnackbar(binding.root, getString(R.string.msg_error))
                    }

                    is UiState.Empty -> {
                        yelloSnackbar(binding.root, getString(R.string.msg_error))
                    }

                    is UiState.Success -> {}
                }
            }.launchIn(lifecycleScope)
    }

    companion object {
        private const val JSON_VOTE_STEP = "vote_step"
        private const val EVENT_VIEW_VOTE_QUESTION = "view_vote_question"
    }
}
