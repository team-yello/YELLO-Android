package com.el.yello.presentation.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemProfileQuitReasonBinding
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

class ProfileQuitReasonAdapter(
    private val storeQuitReason: (String) -> Unit,
    private val setEtcText: (String) -> Unit,
    private val onItemClickListener: (Int, Boolean) -> Unit,
) : ListAdapter<String, ProfileQuitReasonAdapter.QuitReasonViewHolder>(diffUtil) {
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuitReasonViewHolder {
        return QuitReasonViewHolder(
            ItemProfileQuitReasonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeQuitReason,
            setEtcText,
            this::onItemClicked,
        )
    }

    override fun onBindViewHolder(holder: QuitReasonViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedItemPosition)
    }

    private fun onItemClicked(position: Int) {
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedItemPosition)
        }
        selectedItemPosition = position
        notifyItemChanged(position)
        storeQuitReason(getItem(position))
        onItemClickListener(position, true)
    }

    class QuitReasonViewHolder(
        private val binding: ItemProfileQuitReasonBinding,
        private val storeQuitReason: (String) -> Unit,
        private val setEtcText: (String) -> Unit,
        private val onItemClicked: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reason: String, isItemSelected: Boolean) {
            binding.reason = reason
            binding.root.setOnSingleClickListener {
                onItemClicked(absoluteAdapterPosition)
            }
            binding.etQuitEtc.addTextChangedListener {
                setEtcText(binding.etQuitEtc.text.toString())
            }
            if (isItemSelected) {
                with(binding) {
                    ivQuitReasonCheckpoint.setBackgroundResource(R.drawable.ic_profile_quit_reason_check)
                    cvQuitReasonList.setBackgroundResource(R.drawable.shape_black_fill_white_line_8_rect)
                    etQuitEtc.visibility = if (absoluteAdapterPosition == 7) View.VISIBLE else View.GONE
                }
                storeQuitReason(reason)
            } else {
                with(binding) {
                    ivQuitReasonCheckpoint.setBackgroundResource(R.drawable.ic_profile_quit_reason_uncheck)
                    cvQuitReasonList.setBackgroundResource(R.drawable.shape_grayscales900_fill_8_rect)
                    etQuitEtc.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<String>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
