package com.el.yello.presentation.main.yello.vote

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityVoteBinding
import com.el.yello.presentation.main.yello.YelloFragment
import com.el.yello.presentation.main.yello.vote.frame.NoteFrameAdapter
import com.el.yello.presentation.main.yello.vote.note.NoteAdapter
import com.el.yello.util.extension.setCurrentItemWithDuration
import com.el.yello.util.manager.AmplitudeManager
import com.el.yello.util.extension.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.animation.FadeOutTransformation
import com.example.ui.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject

@AndroidEntryPoint
class VoteActivity : BindingActivity<ActivityVoteBinding>(R.layout.activity_vote) {
    private val viewModel by viewModels<VoteViewModel>()

    private val noteAdapter by lazy {
        NoteAdapter(
            fragmentActivity = this,
            voteListSize = viewModel.totalListCount + 1,
        )
    }

    private val noteFrameAdapter by lazy {
        NoteFrameAdapter(
            fragmentActivity = this,
            bgIndex = viewModel.backgroundIndex,
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
                    is UiState.Success -> initNoteViewPager()
                    is UiState.Empty -> {}
                    is UiState.Failure -> {
                        Intent(this, YelloFragment::class.java).apply {
                            setResult(Activity.RESULT_CANCELED, this)
                            if (!isFinishing) finish()
                        }
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun initNoteViewPager() {
        with(binding.vpVoteNote) {
            adapter = noteAdapter
            isUserInputEnabled = false
        }
        with(binding.vpVoteNoteFrame) {
            adapter = noteFrameAdapter
            setPageTransformer(FadeOutTransformation())
            isUserInputEnabled = false
        }
    }

    private fun setupCurrentNoteIndex() {
        viewModel._currentNoteIndex.flowWithLifecycle(lifecycle)
            .onEach { index ->
                binding.vpVoteNote.setCurrentItemWithDuration(index, DURATION_NOTE_TRANSITION)
                binding.vpVoteNoteFrame.setCurrentItemWithDuration(index, DURATION_FRAME_TRANSITION)
                if (index <= viewModel.totalListCount) {
                    val properties = JSONObject().put(JSON_VOTE_STEP, index + 1)
                    AmplitudeManager.trackEventWithProperties(EVENT_VIEW_VOTE_QUESTION, properties)
                }
            }.launchIn(lifecycleScope)
    }

    private fun setupPostVoteState() {
        viewModel.postVoteState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Loading -> {}
                    is UiState.Failure -> {
                        yelloSnackbar(
                            binding.root,
                            getString(R.string.internet_connection_error_msg)
                        )
                    }

                    is UiState.Empty -> {
                        yelloSnackbar(
                            binding.root,
                            getString(R.string.internet_connection_error_msg)
                        )
                    }

                    is UiState.Success -> {}
                }
            }.launchIn(lifecycleScope)
    }

    companion object {
        private const val DURATION_NOTE_TRANSITION = 500L
        private const val DURATION_FRAME_TRANSITION = 400L
        private const val JSON_VOTE_STEP = "vote_step"
        private const val EVENT_VIEW_VOTE_QUESTION = "view_vote_question"
    }
}
