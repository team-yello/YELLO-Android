package com.yello.presentation.main.myyello

import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentMyYelloBinding

class MyYelloFragment : BindingFragment<FragmentMyYelloBinding>(R.layout.fragment_my_yello) {

    companion object {
        @JvmStatic
        fun newInstance() = MyYelloFragment()
    }
}
