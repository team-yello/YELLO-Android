package com.yello.presentation.main.yello.point

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentPointBinding

class PointFragment : BindingFragment<FragmentPointBinding>(R.layout.fragment_point) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PointFragment()
    }
}
