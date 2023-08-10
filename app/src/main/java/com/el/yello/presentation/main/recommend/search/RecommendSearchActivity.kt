package com.el.yello.presentation.main.recommend.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.el.yello.R
import com.el.yello.databinding.ActivityRecommendSearchBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendSearchActivity :
    BindingActivity<ActivityRecommendSearchBinding>(R.layout.activity_recommend_search) {

    private var _adapter: RecommendSearchAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFocusToEditText()
        initAdapterWithDivider()
        initBackBtnListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }

    private fun initAdapterWithDivider() {
        _adapter = RecommendSearchAdapter()
        binding.rvRecommendSearch.adapter = adapter
        binding.rvRecommendSearch.addItemDecoration(
            RecommendSearchItemDecoration(this)
        )
    }

    private fun initFocusToEditText() {
        binding.etRecommendSearchBox.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etRecommendSearchBox, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun initBackBtnListener() {
        binding.btnRecommendSearchBack.setOnSingleClickListener {
            finish()
        }
    }
}