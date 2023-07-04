package com.yello.presentation.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yello.databinding.FragmentStudentidBinding

class StudentIdFragment : Fragment() {
    private var _binding: FragmentStudentidBinding? = null
    private val binding: FragmentStudentidBinding
        get() = requireNotNull(_binding) { " _binding이 null입니다!" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStudentidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 대부분의 로직은 여기에 구현합니다.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
