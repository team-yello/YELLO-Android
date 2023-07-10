package com.yello.presentation.main.yello.vote.note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonSizeSpec
import com.yello.R
import com.yello.databinding.FragmentNoteBinding
import com.yello.presentation.main.yello.vote.VoteViewModel
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
        initIndexBalloon()
    }

    private fun getBundleArgs() {
        arguments ?: return
        _noteIndex = arguments?.getInt(ARGS_NOTE_INDEX)
        binding.index = noteIndex
        _backgroundIndex = arguments?.getInt(ARGS_BACKGROUND_INDEX)?.plus(noteIndex)
        binding.bgIndex = backgroundIndex
    }

    private fun initIndexBalloon() {
        // TODO: balloon custom
        val balloon = Balloon.Builder(requireContext())
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setText("첫 번째 옐로!")
            .build()
    }

    companion object {
        private const val ARGS_NOTE_INDEX = "NOTE_INDEX"
        private const val ARGS_BACKGROUND_INDEX = "BACKGROUND_INDEX"

        @JvmStatic
        fun newInstance(index: Int, bgIndex: Int) = NoteFragment().apply {
            val args = Bundle()
            args.putInt(ARGS_NOTE_INDEX, index)
            args.putInt(ARGS_BACKGROUND_INDEX, bgIndex)
            arguments = args
        }
    }
}
