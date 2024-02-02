package com.el.yello.presentation.main.yello.vote.note

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentNoteBinding
import com.el.yello.presentation.main.yello.vote.NoteState
import com.el.yello.presentation.main.yello.vote.VoteViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NoteFragment : BindingFragment<FragmentNoteBinding>(R.layout.fragment_note) {
    private val viewModel by activityViewModels<VoteViewModel>()

    private var _noteIndex: Int? = null
    private val noteIndex
        get() = _noteIndex ?: 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        getBundleArgs()
    }

    private fun getBundleArgs() {
        arguments ?: return
        _noteIndex = arguments?.getInt(ARGS_NOTE_INDEX)
        binding.index = noteIndex
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
                        getString(R.string.msg_error),
                    )
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        // TODO : 자주 사용되는 상수들 파일로 빼기
        private const val ARGS_NOTE_INDEX = "NOTE_INDEX"

        @JvmStatic
        fun newInstance(index: Int) = NoteFragment().apply {
            val args = bundleOf(
                ARGS_NOTE_INDEX to index,
            )
            arguments = args
        }
    }
}
