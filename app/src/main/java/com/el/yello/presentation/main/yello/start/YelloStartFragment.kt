package com.el.yello.presentation.main.yello.start

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloStartBinding
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.vote.VoteActivity
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloStartFragment :
    BindingFragment<FragmentYelloStartBinding>(R.layout.fragment_yello_start) {
    private val viewModel by activityViewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initEntranceLottie()
        initShadowView()
        initVoteBtnClickListener()
    }

    private fun initEntranceLottie() {
        with(binding.lottieStartEntrance) {
            val size = Point()
            display.getRealSize(size)
            val displayWidth = size.x
            val displayHeight = size.y

            layoutParams.width = (2.22 * displayWidth).toInt()
            setMargins(this, 0, 0, 0, (-0.435 * displayHeight).toInt())
        }
    }

    private fun initShadowView() {
        binding.shadowStart.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }

    private fun initVoteBtnClickListener() {
        binding.btnStartVote.setOnSingleClickListener {
            intentToVoteScreen()
        }
    }

    private fun intentToVoteScreen() {
        Intent(activity, VoteActivity::class.java).apply {
            startActivity(this)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloStartFragment()
    }
}
