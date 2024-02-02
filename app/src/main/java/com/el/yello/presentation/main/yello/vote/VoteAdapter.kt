package com.el.yello.presentation.main.yello.vote

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.el.yello.presentation.main.yello.point.PointFragment
import com.el.yello.presentation.main.yello.vote.note.NoteFragment

class VoteAdapter(
    fragmentActivity: FragmentActivity,
    private val voteListSize: Int,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = voteListSize

    override fun createFragment(position: Int): Fragment {
        return NoteFragment.newInstance(position)
    }
}
