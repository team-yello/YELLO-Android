package com.el.yello.presentation.main.look

import android.os.Bundle
import android.view.View
import com.el.yello.R
import com.el.yello.databinding.FragmentLookBinding
import com.el.yello.presentation.onboarding.fragment.addfriend.AddFriendAdapter
import com.example.ui.base.BindingFragment

class LookFragment : BindingFragment<FragmentLookBinding>(R.layout.fragment_look) {

    private var _adapter: LookAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LookFragment()
    }
}