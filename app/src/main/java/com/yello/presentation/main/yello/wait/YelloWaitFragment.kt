package com.yello.presentation.main.yello.wait

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentYelloWaitBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloWaitFragment : BindingFragment<FragmentYelloWaitBinding>(R.layout.fragment_yello_wait) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloWaitFragment()
    }
}
