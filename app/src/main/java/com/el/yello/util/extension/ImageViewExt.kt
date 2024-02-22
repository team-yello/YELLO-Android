package com.el.yello.util.extension

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.el.yello.R

fun ImageView.loadUrl(url: String) {
    if (url.endsWith(".svg")) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
        return
    }

    load(url)
}

fun ImageView.loadUrlWithCircleCrop(url: String) {
    if (!url.endsWith(".svg")) {
        load(url) {
            transformations(CircleCropTransformation())
        }
        return
    }

    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadUrlWithCircleCrop.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .data(url)
        .transformations(CircleCropTransformation())
        .target(this)
        .build()

    imageLoader.enqueue(request)
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
