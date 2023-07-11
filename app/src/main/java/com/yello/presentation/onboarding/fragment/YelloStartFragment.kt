package com.yello.presentation.onboarding.fragment

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentYelloStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloStartFragment : BindingFragment<FragmentYelloStartBinding>(R.layout.fragment_yello_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloStartFragment()
    }
}
