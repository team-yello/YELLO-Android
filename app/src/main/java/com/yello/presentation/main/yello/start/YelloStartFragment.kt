package com.yello.presentation.main.yello.start

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentYelloStartBinding
import com.yello.presentation.main.yello.vote.VoteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloStartFragment :
    BindingFragment<FragmentYelloStartBinding>(R.layout.fragment_yello_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVoteBtnClickListener()
    }

    private fun initVoteBtnClickListener() {
        binding.btnStartVote.setOnSingleClickListener {
            intentToVoteScreen()
        }
    }

    private fun intentToVoteScreen() {
        Intent(activity, VoteActivity::class.java).apply {
            startActivity(this)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloStartFragment()
    }
}
