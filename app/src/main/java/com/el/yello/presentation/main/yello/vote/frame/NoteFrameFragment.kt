package com.el.yello.presentation.main.yello.vote.frame

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentNoteFrameBinding
import com.el.yello.presentation.main.yello.vote.NoteState
import com.el.yello.presentation.main.yello.vote.VoteViewModel
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject

@AndroidEntryPoint
class NoteFrameFragment : BindingFragment<FragmentNoteFrameBinding>(R.layout.fragment_note_frame) {
    private val viewModel by activityViewModels<VoteViewModel>()

    private var _noteIndex: Int? = null
    private val noteIndex
        get() = _noteIndex ?: 0

    private var _backgroundIndex: Int? = null
    private val backgroundIndex
        get() = _backgroundIndex ?: 0

    private var _voteListSize: Int? = null
    private val voteListSize
        get() = _voteListSize ?: 8

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        getBundleArgs()
        addOvalProgressItems()
        initShuffleBtnClickListener()
        initSkipBtnClickListener()
    }

    private fun getBundleArgs() {
        arguments ?: return
        _noteIndex = arguments?.getInt(ARGS_NOTE_INDEX)
        binding.index = noteIndex
        _backgroundIndex = arguments?.getInt(ARGS_BACKGROUND_INDEX)?.plus(noteIndex)
        binding.bgIndex = backgroundIndex
        _voteListSize = arguments?.getInt(ARGS_VOTE_LIST_SIZE)
    }

    private fun addOvalProgressItems() {
        for (i in 0 until noteIndex) {
            layoutInflater.inflate(
                R.layout.layout_vote_progress_bar,
                binding.layoutNoteProgressBefore,
            )
            binding.layoutNoteProgressBefore.getChildAt(i).rotation = progressDegree[i]
        }
        for (i in noteIndex + 1 until voteListSize) {
            layoutInflater.inflate(
                R.layout.layout_vote_progress_bar,
                binding.layoutNoteProgressAfter,
            )
            binding.layoutNoteProgressAfter.getChildAt(i - noteIndex - 1).rotation =
                progressDegree[i]
        }
    }

    private fun initShuffleBtnClickListener() {
        binding.btnVoteShuffle.setOnSingleClickListener {
            viewModel.shuffle()
            if (noteIndex in 1..8) {
                val properties = JSONObject().put(JSON_QUESTION_ID, noteIndex + 1)
                AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_VOTE_SHUFFLE, properties)
            }
        }
    }

    private fun initSkipBtnClickListener() {
        binding.btnVoteSkip.setOnSingleClickListener {
            viewModel.skip()
            if (noteIndex in 1..8) {
                val properties = JSONObject().put(JSON_QUESTION_ID, noteIndex + 1)
                AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_VOTE_SKIP, properties)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        setupVoteState()
    }

    private fun setupVoteState() {
        viewModel.noteState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    NoteState.Success -> return@onEach
                    NoteState.InvalidSkip -> yelloSnackbar(
                        binding.root,
                        getString(R.string.note_msg_invalid_skip),
                    )

                    NoteState.InvalidCancel -> yelloSnackbar(
                        binding.root,
                        getString(R.string.note_msg_invalid_cancel),
                    )

                    NoteState.InvalidShuffle -> yelloSnackbar(
                        binding.root,
                        getString(R.string.note_msg_invalid_shuffle),
                    )

                    NoteState.InvalidName -> yelloSnackbar(
                        binding.root,
                        getString(R.string.note_msg_invalid_name),
                    )

                    NoteState.Failure -> yelloSnackbar(
                        binding.root,
                        getString(R.string.internet_connection_error_msg),
                    )
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        private const val ARGS_NOTE_INDEX = "NOTE_INDEX"
        private const val ARGS_BACKGROUND_INDEX = "BACKGROUND_INDEX"
        private const val ARGS_VOTE_LIST_SIZE = "VOTE_LIST_SIZE"

        private const val JSON_QUESTION_ID = "question_id"

        private const val EVENT_CLICK_VOTE_SHUFFLE = "click_vote_shuffle"
        private const val EVENT_CLICK_VOTE_SKIP = "click_vote_skip"

        private val progressDegree =
            listOf(165f, -30f, -120f, -165f, -60f, -20f, -117f, 24f, -45f, 12f)

        @JvmStatic
        fun newInstance(index: Int, bgIndex: Int, voteListSize: Int) = NoteFrameFragment().apply {
            val args = bundleOf(
                ARGS_NOTE_INDEX to index,
                ARGS_BACKGROUND_INDEX to bgIndex,
                ARGS_VOTE_LIST_SIZE to voteListSize,
            )
            arguments = args
        }
    }
}
