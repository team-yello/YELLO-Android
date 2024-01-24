package com.el.yello.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import coil.transform.CircleCropTransformation
import com.el.yello.R

object Utils {
    fun setChosungText(name: String, number: Int): String {
        val firstChosung = name[number]
        val chosungUnicode = Character.UnicodeBlock.of(firstChosung.toInt())
        return if (chosungUnicode == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO ||
            chosungUnicode == Character.UnicodeBlock.HANGUL_JAMO ||
            chosungUnicode == Character.UnicodeBlock.HANGUL_SYLLABLES
        ) {
            val chosungIndex = (firstChosung.toInt() - 0xAC00) / 28 / 21
            val chosung = Character.toChars(chosungIndex + 0x1100)[0]
            chosung.toString() // 출력: "ㄱ"
        } else {
            ""
        }
    }

    fun SwipeRefreshLayout.setPullToScrollColor(arrowColor: Int, bgColor: Int) {
        setColorSchemeColors(ContextCompat.getColor(context, arrowColor))
        setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, bgColor))
    }

    fun ImageView.setImageOrBasicThumbnail(thumbnail: String) {
        this.apply {
            load(if (thumbnail == BASIC_THUMBNAIL) R.drawable.img_yello_basic else thumbnail) {
                transformations(CircleCropTransformation())
            }
        }
    }

    const val BASIC_THUMBNAIL =
        "https://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg"

}
