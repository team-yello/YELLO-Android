package com.yello.util.binding

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.BindingAdapter
import com.yello.R

object BindingAdapter {

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

    @JvmStatic
    @BindingAdapter("selectedIndex", "optionIndex")
    fun TextView.setNameTextColor(selectedIndex: Int?, optionIndex: Int) {
        if (selectedIndex == null) {
            setTextColor(context.getColor(R.color.white))
            return
        }

        if (selectedIndex == optionIndex) {
            setTextColor(context.getColor(R.color.yello_main_500))
            return
        }

        setTextColor(context.getColor(R.color.grayscales_700))
    }

    @JvmStatic
    @BindingAdapter("selectedId", "optionId")
    fun TextView.setYelloIdTextColor(selectedId: Int?, optionId: Int) {
        if (selectedId != null && selectedId != optionId) {
            setTextColor(context.getColor(R.color.grayscales_800))
            return
        }
        setTextColor(context.getColor(R.color.grayscales_600))
    }

    @JvmStatic
    @BindingAdapter("selectedKeyword", "optionKeyword")
    fun TextView.setKeywordTextColor(selectedKeyword: String?, optionKeyword: String) {
        if (selectedKeyword == null) {
            setTextColor(context.getColor(R.color.white))
            return
        }

        if (selectedKeyword == optionKeyword) {
            setTextColor(context.getColor(R.color.yello_main_500))
            return
        }

        setTextColor(context.getColor(R.color.grayscales_700))
    }

    @JvmStatic
    @BindingAdapter("setDrawableTint")
    fun TextView.setDrawableTint(disabled: Boolean) {
        val color =
            if (disabled) {
                getColor(context, R.color.gray_66)
            } else {
                getColor(
                    context,
                    R.color.black,
                )
            }
        for (drawable in compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setBalloonSrc")
    fun ImageView.setBalloonSrc(index: Int) {
        setImageResource(
            when (index) {
                0 -> R.drawable.ic_note_balloon1
                1 -> R.drawable.ic_note_balloon2
                2 -> R.drawable.ic_note_balloon3
                3 -> R.drawable.ic_note_balloon4
                4 -> R.drawable.ic_note_balloon5
                5 -> R.drawable.ic_note_balloon6
                6 -> R.drawable.ic_note_balloon7
                7 -> R.drawable.ic_note_balloon8
                8 -> R.drawable.ic_note_balloon9
                else -> R.drawable.ic_note_balloon10
            },
        )
    }

    @JvmStatic
    @BindingAdapter("setFaceSrc")
    fun ImageView.setFaceSrc(index: Int) {
        setImageResource(
            when (index) {
                0 -> R.drawable.img_note_face1
                1 -> R.drawable.img_note_face2
                2 -> R.drawable.img_note_face3
                3 -> R.drawable.img_note_face4
                4 -> R.drawable.img_note_face5
                5 -> R.drawable.img_note_face6
                6 -> R.drawable.img_note_face7
                7 -> R.drawable.img_note_face8
                8 -> R.drawable.img_note_face9
                else -> R.drawable.img_note_face10
            },
        )
    }

    @JvmStatic
    @BindingAdapter("convertToMinAndSec")
    fun TextView.convertToMinAndSec(sec: Int) {
        val minutes = sec / 60
        val seconds = sec % 60
        text = String.format(context.getString(R.string.wait_time_format), minutes, seconds)
    }

    // 친구 추가 뷰 profile
    @JvmStatic
    @BindingAdapter("setCircleImage")
    fun ImageView.setCircleImage(index: Int) {
        setImageResource(
            when (index) {
                0 -> R.drawable.img_note_face1
                1 -> R.drawable.img_note_face2
                2 -> R.drawable.img_note_face3
                3 -> R.drawable.img_note_face4
                4 -> R.drawable.img_note_face5
                5 -> R.drawable.img_note_face6
                6 -> R.drawable.img_note_face7
                7 -> R.drawable.img_note_face8
                8 -> R.drawable.img_note_face9
                else -> R.drawable.img_note_face10
            },
        )
    }
}
