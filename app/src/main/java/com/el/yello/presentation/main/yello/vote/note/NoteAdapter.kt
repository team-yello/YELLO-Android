package com.el.yello.presentation.main.yello.vote.note

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class NoteAdapter(
    fragmentActivity: FragmentActivity,
    private val voteListSize: Int,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = voteListSize + COUNT_POINT_FRAGMENT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            in INDEX_START_POSITION until voteListSize -> NoteFragment.newInstance(position)
            else -> Fragment()
        }
    }

    companion object {
        private const val INDEX_START_POSITION = 0
        private const val COUNT_POINT_FRAGMENT = 1
    }
}
