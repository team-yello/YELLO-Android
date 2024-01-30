package com.el.yello.presentation.main.profile.detail

import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileSchoolDetailBinding
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolProfileDetailActivity :
    BindingActivity<ActivityProfileSchoolDetailBinding>(R.layout.activity_profile_school_detail) {

    private val viewModel by viewModels<SchoolProfileDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}