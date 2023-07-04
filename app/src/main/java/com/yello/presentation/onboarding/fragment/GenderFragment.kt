package com.yello.presentation.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yello.databinding.FragmentGenderBinding

class GenderFragment : Fragment() {
    private var _binding: FragmentGenderBinding? = null
    private val binding: FragmentGenderBinding
        get() = requireNotNull(_binding) { " _binding이 null입니다!" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO 대부분의 로직은 여기에 구현합니다.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
