package com.el.yello.presentation.main.yello

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloBinding
import com.el.yello.presentation.main.yello.lock.YelloLockFragment
import com.el.yello.presentation.main.yello.start.YelloStartFragment
import com.el.yello.presentation.main.yello.vote.VoteActivity
import com.el.yello.presentation.main.yello.wait.YelloWaitFragment
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.entity.type.YelloState.Lock
import com.example.domain.entity.type.YelloState.Valid
import com.example.domain.entity.type.YelloState.Wait
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.toast
import com.example.ui.view.UiState.Empty
import com.example.ui.view.UiState.Failure
import com.example.ui.view.UiState.Loading
import com.example.ui.view.UiState.Success
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloFragment : BindingFragment<FragmentYelloBinding>(R.layout.fragment_yello) {
    private val viewModel by activityViewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupYelloState()
        checkStoredVote()
    }

    private fun setupYelloState() {
        viewModel.yelloState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loading -> {}
                is Success -> {
                    when (state.data) {
                        is Lock -> navigateTo<YelloLockFragment>()
                        is Valid -> navigateTo<YelloStartFragment>()
                        is Wait -> navigateTo<YelloStartFragment>()
                    }
                }

                is Empty -> {
                    yelloSnackbar(
                        binding.root,
                        getString(R.string.msg_failure),
                    )
                }

                is Failure -> {
                    toast(getString(R.string.msg_auto_login_error))
                    restartApp(requireContext())
                }
            }
        }
    }

    private inline fun <reified T : Fragment> navigateTo() {
        requireActivity().supportFragmentManager.commit {
            replace<T>(R.id.fcv_yello, T::class.java.canonicalName)
        }
    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val componentName = packageManager.getLaunchIntentForPackage(packageName)?.component
        context.startActivity(Intent.makeRestartActivityTask(componentName))
        Runtime.getRuntime().exit(0)
    }

    private fun checkStoredVote() {
        viewModel.getStoredVote() ?: return
        intentToVoteScreen()
    }

    private fun intentToVoteScreen() {
        Intent(activity, VoteActivity::class.java).apply {
            startActivity(this)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getVoteState()
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloFragment()
    }
}
