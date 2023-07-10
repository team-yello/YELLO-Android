package com.yello.presentation.main.yello.vote

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yello.presentation.main.yello.vote.note.NoteFragment

class VoteAdapter(
    fragmentActivity: FragmentActivity,
    private val bgIndex: Int,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = COUNT_NOTE_FRAGMENT

    override fun createFragment(position: Int): Fragment =
        NoteFragment.newInstance(position, bgIndex)

    companion object {
        private const val COUNT_NOTE_FRAGMENT = 10
    }
}
