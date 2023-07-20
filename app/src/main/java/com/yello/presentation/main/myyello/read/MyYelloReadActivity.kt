package com.yello.presentation.main.myyello.read

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.domain.entity.YelloDetail
import com.example.domain.enum.PointEnum
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.intent.boolExtra
import com.example.ui.intent.intExtra
import com.example.ui.intent.longExtra
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ActivityMyYelloReadBinding
import com.yello.presentation.pay.PayActivity
import com.yello.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class MyYelloReadActivity :
    BindingActivity<ActivityMyYelloReadBinding>(R.layout.activity_my_yello_read) {
    private val id by longExtra()
    private val nameIndex by intExtra()
    private val isHintUsed by boolExtra()
    private val viewModel by viewModels<MyYelloReadViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initClick()
        observe()
    }

    private fun initView() {
        viewModel.getYelloDetail(id)
        viewModel.setNameIndex(nameIndex)
        viewModel.setHintUsed(isHintUsed)
    }

    private fun initClick() {
        binding.tvInitialCheck.setOnSingleClickListener {
            viewModel.setIsFinishCheck(false)
            PointUseDialog.newInstance(
                if (binding.tvInitialCheck.text.toString()
                        .contains("300")
                ) viewModel.myPoint > 300 else viewModel.myPoint > 100,
                if (binding.tvInitialCheck.text.toString()
                        .contains("300")
                ) PointEnum.INITIAL.ordinal else PointEnum.KEYWORD.ordinal
            ).show(supportFragmentManager, "dialog")
        }

        binding.clSendCheck.setOnSingleClickListener {
            Intent(this, PayActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.ivBack.setOnSingleClickListener {
            finish()
        }

        binding.clInstagram.setOnSingleClickListener {
            setViewInstagram(true)
            lifecycleScope.launch {
                delay(500)
                shareInstagramStory()
            }
        }
    }

    private fun observe() {
        viewModel.yelloDetailData.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        binding.data = it.data
                        setData(it.data)
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)

        viewModel.isFinishCheck.flowWithLifecycle(lifecycle)
            .onEach {
                lifecycleScope.launch {
                    delay(300)
                    if (it) viewModel.getYelloDetail(id)
                }

            }.launchIn(lifecycleScope)
    }

    private fun setViewInstagram(isInstagram: Boolean) {
        binding.clInstagramView.isVisible = isInstagram
        binding.clTopView.isVisible = !isInstagram
        binding.clBottomView.isVisible = !isInstagram
    }

    private fun setData(yello: YelloDetail) {
        binding.tvSendName.isVisible = yello.nameHint != -1
        binding.tvNameNotYet.isVisible = yello.nameHint == -1
        if (yello.nameHint >= 0) binding.tvSendName.text =
            Utils.setChosungText(yello.senderName, yello.nameHint)
        binding.tvInitialCheck.isVisible = !(yello.nameHint >= 0 && yello.isAnswerRevealed)
        binding.tvGender.text =
            if (yello.senderGender.contains("MALE")) getString(R.string.my_yello_male) else getString(
                R.string.my_yello_female
            )
        binding.tvInitialCheck.text =
            if (yello.isAnswerRevealed) "300포인트로 초성 1개 확인하기" else "100포인트로 키워드 확인하기"
        viewModel.setMyPoint(yello.currentPoint)
    }

    private fun shareInstagramStory() {
        val backgroundAssetUri = makeUriFromView(binding.layout)
        // Instantiate an intent
        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            setDataAndType(backgroundAssetUri, "image/jpeg")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        try {
            instagramLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            val playStoreIntent = Intent(Intent.ACTION_VIEW)
            playStoreIntent.data = Uri.parse("market://details?id=com.instagram.android")
            startActivity(playStoreIntent)
        }
    }

    private fun makeUriFromView(view: View): Uri? {
        val bitmap = makeBitmapFromView(view)

        // temp file의 이름을 정합니다.
        // 확장자_YYYYMMDDHHMMSS
        val time = System.currentTimeMillis()
        val dayTime = SimpleDateFormat("yyyymmddhhmmss")
        val fileName = "JPEG_" + dayTime.format(Date(time)) + "_" + time.toString()

        val bytes = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, fileName, null)

        return Uri.parse(path)
    }

    private fun makeBitmapFromView(view: View): Bitmap {
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private val instagramLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        setViewInstagram(false)
    }

    override fun finish() {
        viewModel.isHintUsed?.let {
            intent.putExtra("isHintUsed", it)
        }
        viewModel.nameIndex?.let {
            intent.putExtra("nameIndex", it)
        }
        setResult(RESULT_OK, intent)
        super.finish()
    }

    companion object {
        fun getIntent(context: Context, id: Long, nameIndex: Int, isHintUsed: Boolean) =
            Intent(context, MyYelloReadActivity::class.java)
                .putExtra("id", id)
                .putExtra("nameIndex", nameIndex)
                .putExtra("isHintUsed", isHintUsed)
    }
}