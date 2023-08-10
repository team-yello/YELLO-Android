package com.el.yello.presentation.main.recommend.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.el.yello.R
import com.el.yello.databinding.ActivityRecommendSearchBinding
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendSearchActivity :
    BindingActivity<ActivityRecommendSearchBinding>(R.layout.activity_recommend_search) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFocusToEditText()

    }

    private fun initFocusToEditText() {
        binding.etRecommendSearchBox.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etRecommendSearchBox, InputMethodManager.SHOW_IMPLICIT)
    }
}
