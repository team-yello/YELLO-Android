package com.yello.presentation.main.yello

import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentYelloBinding

class YelloFragment : BindingFragment<FragmentYelloBinding>(R.layout.fragment_yello) {

    companion object {
        @JvmStatic
        fun newInstance() = YelloFragment()
    }
}
