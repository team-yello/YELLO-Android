package com.yello.presentation.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentCodeBinding

class CodeFragment : BindingFragment<FragmentCodeBinding>(R.layout.fragment_code) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_code, container, false)
    }
}
