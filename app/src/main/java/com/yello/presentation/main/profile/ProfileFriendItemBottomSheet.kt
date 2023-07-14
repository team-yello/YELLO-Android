package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileFriendItemBottomSheetBinding

class ProfileFriendItemBottomSheet :
    BindingBottomSheetDialog<FragmentProfileFriendItemBottomSheetBinding>(R.layout.fragment_profile_friend_item_bottom_sheet) {

    private lateinit var modelName: String
    private lateinit var modelId: String
    private lateinit var modelSchool: String
    private lateinit var modelThumbnail: String
    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getItemData()
        setItemData()
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
        binding.tvProfileFriendName.text = modelName
        binding.tvProfileFriendId.text = modelId
        binding.tvProfileFriendSchool.text = modelSchool
        if (modelThumbnail != "") {
            binding.ivProfileFriendThumbnail.load(modelThumbnail) {
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun initDeleteButton() {
        binding.btnProfileFriendDelete.setOnSingleClickListener {
            dismiss()
            ProflieFriendDeleteBottomSheet.newInstance(
                modelName,
                modelId,
                modelSchool,
                modelThumbnail
            ).show(parentFragmentManager, "Dialog")
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
            ProfileFriendItemBottomSheet().apply {
                val args = Bundle()
                args.putString("modelName", modelName)
                args.putString("modelId", modelId)
                args.putString("modelSchool", modelSchool)
                args.putString("modelThumbnail", modelThumbnail)
                arguments = args
            }
    }
}