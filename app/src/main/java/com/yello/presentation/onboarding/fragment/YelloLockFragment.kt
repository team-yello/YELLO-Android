package com.yello.presentation.onboarding.fragment

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentYelloLockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloLockFragment : BindingFragment<FragmentYelloLockBinding>(R.layout.fragment_yello_lock) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloLockFragment()
    }
}
