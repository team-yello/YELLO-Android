package com.el.yello.util.binding

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.el.yello.R

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
    fun LottieAnimationView.setBalloonSrc(index: Int) {
        setAnimation(
            when (index) {
                0 -> R.raw.lottie_note_balloon1
                1 -> R.raw.lottie_note_balloon2
                2 -> R.raw.lottie_note_balloon3
                3 -> R.raw.lottie_note_balloon4
                4 -> R.raw.lottie_note_balloon5
                5 -> R.raw.lottie_note_balloon6
                6 -> R.raw.lottie_note_balloon7
                else -> R.raw.lottie_note_balloon8
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
    fun TextView.convertToMinAndSec(left: Long) {
        val minutes = left / 60
        val seconds = left % 60
        text = String.format(context.getString(R.string.wait_time_format), minutes, seconds)
    }

    @JvmStatic
    @BindingAdapter("setNullOrBlankVisible")
    fun TextView.setNullOrBlankVisible(text: String?) {
        this.isVisible = !text.isNullOrBlank()
    }

    @JvmStatic
    @BindingAdapter("setImageTint")
    fun ImageView.setImageTint(colorIndex: Int) {
        if (colorIndex == 1 || colorIndex == 3 || colorIndex == 7) {
            this.setColorFilter(getColor(this.context, R.color.black))
        }
    }

    @JvmStatic
    @BindingAdapter("setTextTint")
    fun TextView.setTextTint(colorIndex: Int) {
        if (colorIndex == 1 || colorIndex == 3 || colorIndex == 7) {
            this.setTextColor(getColor(this.context, R.color.black))
        }
    }

    @JvmStatic
    @BindingAdapter("android:layout_weight")
    fun View.setLayoutWeight(weight: Int) {
        val layoutParams = layoutParams as? LinearLayout.LayoutParams
        layoutParams?.let {
            it.weight = weight.toFloat()
            this.layoutParams = it
        }
    }
}
