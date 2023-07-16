package com.yello.presentation.main.yello.vote

import android.os.Bundle
import androidx.activity.viewModels
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.transformation.FadeOutTransformation
import com.example.ui.view.UiState
import com.yello.R
import com.yello.databinding.ActivityVoteBinding
import com.yello.presentation.util.setCurrentItemWithDuration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoteActivity : BindingActivity<ActivityVoteBinding>(R.layout.activity_vote) {
    private val viewModel by viewModels<VoteViewModel>()

    private val voteAdapter by lazy { VoteAdapter(this, viewModel.backgroundIndex) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        setupVoteState()
        setupCurrentNoteIndex()
    }

    private fun setupVoteState() {
        viewModel._voteState.observe(this) { state ->
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
            binding.vpVote.setCurrentItemWithDuration(index, 400)
        }
    }
}
