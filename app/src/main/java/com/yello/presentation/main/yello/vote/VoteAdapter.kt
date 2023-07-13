package com.yello.presentation.main.yello.vote

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yello.presentation.main.yello.point.PointFragment
import com.yello.presentation.main.yello.vote.note.NoteFragment

class VoteAdapter(
    fragmentActivity: FragmentActivity,
    private val bgIndex: Int,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = COUNT_NOTE_FRAGMENT + COUNT_POINT_FRAGMENT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            in INDEX_START_POSITION until COUNT_NOTE_FRAGMENT -> NoteFragment.newInstance(
                position,
                bgIndex,
            )

            else -> PointFragment.newInstance()
        }
    }

    companion object {
        private const val INDEX_START_POSITION = 0

        private const val COUNT_NOTE_FRAGMENT = 10
        private const val COUNT_POINT_FRAGMENT = 1
    }
}
