package com.yello.presentation.onboarding.fragment.nameid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentNameIdBinding
import com.yello.presentation.onboarding.fragment.addfriend.AddFriendViewModel

class NameIdFragment : BindingFragment<FragmentNameIdBinding>(R.layout.fragment_name_id) {

    private val viewModel by viewModels<NameidViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO 대부분의 로직은 여기에 구현합니다.
    }
}
