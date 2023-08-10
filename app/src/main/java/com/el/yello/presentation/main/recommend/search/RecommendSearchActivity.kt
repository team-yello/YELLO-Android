package com.el.yello.presentation.main.recommend.search

import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityRecommendSearchBinding
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendSearchActivity :
    BindingActivity<ActivityRecommendSearchBinding>(R.layout.activity_recommend_search) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
