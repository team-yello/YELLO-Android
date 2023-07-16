package com.yello.util.view

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.yello.R
import com.yello.databinding.LayoutYelloSnackbarBinding

class YelloSnackbar(view: View, private val message: String) {
    private val context = view.context
    private val snackbar = Snackbar.make(view, "", DURATION_YELLO_SNACKBAR)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: LayoutYelloSnackbarBinding =
        DataBindingUtil.inflate(inflater, R.layout.layout_yello_snackbar, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initData() {
        snackbarBinding.tvSnackbar.text = message
    }

    fun show() {
        snackbar.show()
    }

    companion object {
        private const val DURATION_YELLO_SNACKBAR = 1500

        @JvmStatic
        fun make(view: View, message: String) = YelloSnackbar(view, message)
    }
}
