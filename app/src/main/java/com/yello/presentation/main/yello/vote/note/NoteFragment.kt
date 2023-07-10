package com.yello.presentation.main.yello.vote.note

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonSizeSpec
import com.yello.R
import com.yello.databinding.FragmentNoteBinding

class NoteFragment : BindingFragment<FragmentNoteBinding>(R.layout.fragment_note) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initIndexBalloon()
    }

    private fun initIndexBalloon() {
        val balloon = Balloon.Builder(requireContext())
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setText("첫 번째 옐로!")
            // TODO : balloon custom
            .build()
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}
