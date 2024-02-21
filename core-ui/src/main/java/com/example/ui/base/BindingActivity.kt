package com.example.ui.base

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.ui.extension.hideKeyboard

abstract class BindingActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
) : AppCompatActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP){
            val currentFocus = currentFocus
            if (currentFocus != null && isTouchOutsideView(currentFocus, ev)){
                hideKeyboard(currentFocus)
                currentFocus.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isTouchOutsideView(view: View, ev: MotionEvent): Boolean {
        val outRect = Rect(view.left, view.top, view.right, view.bottom)
        return !outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())
    }
}
