package com.yello.presentation.main.yello.vote.note

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentNoteBinding

class NoteFragment : BindingFragment<FragmentNoteBinding>(R.layout.fragment_note) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}
