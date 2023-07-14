package com.yello.presentation.main.yello.vote

import android.os.Bundle
import androidx.activity.viewModels
import com.example.ui.base.BindingActivity
import com.example.ui.transformation.FadeOutTransformation
import com.yello.R
import com.yello.databinding.ActivityVoteBinding
import com.yello.util.context.yelloSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoteActivity : BindingActivity<ActivityVoteBinding>(R.layout.activity_vote) {
    private val viewModel by viewModels<VoteViewModel>()

    private val voteAdapter by lazy { VoteAdapter(this, viewModel.backgroundIndex) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        initVoteViewPager()
        setupCurrentNoteIndex()
    }

    private fun setupCurrentNoteIndex() {
        viewModel._currentNoteIndex.observe(this) { index ->
            binding.vpVote.currentItem = index
        }
    }

    private fun initVoteViewPager() {
        with(binding) {
            vpVote.adapter = voteAdapter
            vpVote.setPageTransformer(FadeOutTransformation())
            vpVote.isUserInputEnabled = false
        }
    }
}
