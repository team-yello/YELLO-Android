package com.el.yello.util

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import com.el.yello.R
import com.el.yello.util.Image.loadUrlWithCircleCrop
import com.example.ui.context.colorOf

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
        setColorSchemeColors(context.colorOf(arrowColor))
        setProgressBackgroundColorSchemeColor(context.colorOf(bgColor))
    }

    fun ImageView.setImageOrBasicThumbnail(thumbnail: String) {
        this.apply {
            if (thumbnail == URL_BASIC_THUMBNAIL) {
                load(R.drawable.img_yello_basic)
            } else {
                loadUrlWithCircleCrop(thumbnail)
            }
        }
    }

    private const val URL_BASIC_THUMBNAIL =
        "https://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg"

}
