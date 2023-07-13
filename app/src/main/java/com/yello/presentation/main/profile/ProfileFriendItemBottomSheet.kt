package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import coil.load
import coil.transform.CircleCropTransformation
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileFriendItemBottomSheetBinding

class ProfileFriendItemBottomSheet :
    BindingBottomSheetDialog<FragmentProfileFriendItemBottomSheetBinding>(R.layout.fragment_profile_friend_item_bottom_sheet) {

    private val profileFriendDeleteBottomSheet: ProflieFriendDeleteBottomSheet =
        ProflieFriendDeleteBottomSheet()

    private lateinit var modelName: String
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
        initDeleteButton()
    }

    private fun getItemData() {
        val arg = arguments ?: return
        modelName = arg.getString("modelName") ?: ""
        modelSchool = arg.getString("modelSchool") ?: ""
        modelThumbnail = arg.getString("modelThumbnail") ?: ""
    }

    private fun setItemData() {
        binding.tvProfileFriendName.text = modelName
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
            profileFriendDeleteBottomSheet.show(parentFragmentManager, "Dialog")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(modelName: String, modelSchool: String, modelThumbnail: String) =
            ProfileFriendItemBottomSheet().apply {
                val args = Bundle()
                args.putString("modelName", modelName)
                args.putString("modelSchool", modelSchool)
                args.putString("modelThumbnail", modelThumbnail)
                arguments = args
            }
    }
}