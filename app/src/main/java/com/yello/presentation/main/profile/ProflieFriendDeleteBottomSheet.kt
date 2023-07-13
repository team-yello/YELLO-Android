package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import coil.load
import coil.transform.CircleCropTransformation
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileFriendDeleteBottomSheetBinding

class ProflieFriendDeleteBottomSheet :
    BindingBottomSheetDialog<FragmentProfileFriendDeleteBottomSheetBinding>(R.layout.fragment_profile_friend_delete_bottom_sheet) {

    private lateinit var modelName: String
    private lateinit var modelId: String
    private lateinit var modelSchool: String
    private lateinit var modelThumbnail: String

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getItemData()
        setItemData()
        initReturnButton()
        initDeleteButton()
    }

    private fun getItemData() {
        val arg = arguments ?: return
        modelName = arg.getString("modelName") ?: ""
        modelId = arg.getString("modelId") ?: ""
        modelSchool = arg.getString("modelSchool") ?: ""
        modelThumbnail = arg.getString("modelThumbnail") ?: ""
    }

    private fun setItemData() {
        binding.tvProfileFriendDeleteName.text = modelName
        binding.tvProfileFriendDeleteId.text = modelId
        binding.tvProfileFriendDeleteSchool.text = modelSchool
        if (modelThumbnail != "") {
            binding.ivProfileFriendDeleteThumbnail.load(modelThumbnail) {
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun initReturnButton() {
        binding.btnProfileFriendDeleteReturn.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initDeleteButton() {
        binding.btnProfileFriendDeleteResume.setOnSingleClickListener {
            // TODO: 친구 삭제 로직 구현
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            modelName: String,
            modelId: String,
            modelSchool: String,
            modelThumbnail: String
        ) =
            ProflieFriendDeleteBottomSheet().apply {
                val args = Bundle()
                args.putString("modelName", modelName)
                args.putString("modelId", modelId)
                args.putString("modelSchool", modelSchool)
                args.putString("modelThumbnail", modelThumbnail)
                arguments = args
            }
    }
}