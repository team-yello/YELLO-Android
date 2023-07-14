package com.yello.presentation.main.yello.vote.note

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentNoteBinding
import com.yello.presentation.main.yello.vote.VoteViewModel
import com.yello.util.context.yelloSnackbar
import com.yello.util.view.YelloSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : BindingFragment<FragmentNoteBinding>(R.layout.fragment_note) {
    private val viewModel by activityViewModels<VoteViewModel>()

    private var _noteIndex: Int? = null
    private val noteIndex
        get() = _noteIndex ?: 0

    private var _backgroundIndex: Int? = null
    private val backgroundIndex
        get() = _backgroundIndex ?: 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        getBundleArgs()
        addOvalProgressItems()

        binding.ivNoteFace.setOnSingleClickListener {
            yelloSnackbar(binding.root, "테스트입니당")
        }
    }

    private fun getBundleArgs() {
        arguments ?: return
        _noteIndex = arguments?.getInt(ARGS_NOTE_INDEX)
        binding.index = noteIndex
        _backgroundIndex = arguments?.getInt(ARGS_BACKGROUND_INDEX)?.plus(noteIndex)
        binding.bgIndex = backgroundIndex
    }

    private fun addOvalProgressItems() {
        for (i in 0 until noteIndex) {
            layoutInflater.inflate(
                R.layout.layout_vote_progress_bar,
                binding.layoutNoteProgressBefore,
            )
            binding.layoutNoteProgressBefore.getChildAt(i).rotation = progressDegree[i]
        }

        for (i in noteIndex + 1 until 10) {
            layoutInflater.inflate(
                R.layout.layout_vote_progress_bar,
                binding.layoutNoteProgressAfter,
            )
            binding.layoutNoteProgressAfter.getChildAt(i - noteIndex - 1).rotation =
                progressDegree[i]
        }
    }

    companion object {
        private const val ARGS_NOTE_INDEX = "NOTE_INDEX"
        private const val ARGS_BACKGROUND_INDEX = "BACKGROUND_INDEX"

        private val progressDegree =
            listOf(165f, -30f, -120f, -165f, -60f, -20f, -117f, 24f, -45f, 12f)

        @JvmStatic
        fun newInstance(index: Int, bgIndex: Int) = NoteFragment().apply {
            val args = bundleOf(
                ARGS_NOTE_INDEX to index,
                ARGS_BACKGROUND_INDEX to bgIndex,
            )
            arguments = args
        }
    }
}
