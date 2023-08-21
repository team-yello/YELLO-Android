package com.el.yello.presentation.main.yello.vote

import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityVoteBinding
import com.el.yello.presentation.util.setCurrentItemWithDuration
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.transformation.FadeOutTransformation
import com.example.ui.view.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoteActivity : BindingActivity<ActivityVoteBinding>(R.layout.activity_vote) {
    private val viewModel by viewModels<VoteViewModel>()

    private val voteAdapter by lazy {
        VoteAdapter(
            this,
            viewModel.backgroundIndex,
            viewModel.totalListCount + 1,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        setupVoteState()
        setupCurrentNoteIndex()
        setupPostVoteState()
    }

    private fun setupVoteState() {
        viewModel.voteState.observe(this) { state ->
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
        }
    }

    private fun initVoteViewPager() {
        with(binding) {
            vpVote.adapter = voteAdapter
            vpVote.setPageTransformer(FadeOutTransformation())
            vpVote.isUserInputEnabled = false
        }
    }

    private fun setupCurrentNoteIndex() {
        viewModel._currentNoteIndex.observe(this) { index ->
            binding.vpVote.setCurrentItemWithDuration(index, DURATION_NOTE_TRANSITION)
        }
    }

    private fun setupPostVoteState() {
        viewModel.postVoteState.observe(this) { state ->
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
        }
    }

    companion object {
        private const val DURATION_NOTE_TRANSITION = 400L
    }
}
