package com.el.yello.presentation.main.yello.vote.note

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentNoteBinding
import com.el.yello.presentation.main.yello.vote.VoteViewModel
import com.example.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

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
