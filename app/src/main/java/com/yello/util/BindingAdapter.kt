package com.yello.util

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.yello.R

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("setVoteBackground")
    fun ConstraintLayout.setVoteBackground(bgIndex: Int) {
        setBackgroundResource(
            when (bgIndex % 12) {
                0 -> R.drawable.shape_gradient_vote_bg_01
                1 -> R.drawable.shape_gradient_vote_bg_02
                2 -> R.drawable.shape_gradient_vote_bg_03
                3 -> R.drawable.shape_gradient_vote_bg_04
                4 -> R.drawable.shape_gradient_vote_bg_05
                5 -> R.drawable.shape_gradient_vote_bg_06
                6 -> R.drawable.shape_gradient_vote_bg_07
                7 -> R.drawable.shape_gradient_vote_bg_08
                8 -> R.drawable.shape_gradient_vote_bg_09
                9 -> R.drawable.shape_gradient_vote_bg_10
                10 -> R.drawable.shape_gradient_vote_bg_11
                11 -> R.drawable.shape_gradient_vote_bg_12
                else -> throw IndexOutOfBoundsException("vote bg index out of bounds : $bgIndex")
            },
        )
    }
}
