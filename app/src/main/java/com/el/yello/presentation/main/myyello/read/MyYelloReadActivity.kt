package com.el.yello.presentation.main.myyello.read

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.ActivityMyYelloReadBinding
import com.el.yello.presentation.pay.PayActivity
import com.el.yello.util.Utils
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.YelloDetail
import com.example.domain.enum.PointEnum
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.intent.boolExtra
import com.example.ui.intent.intExtra
import com.example.ui.intent.longExtra
import com.example.ui.restart.restartApp
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
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
        trackAmplitudeEvent()
    }

    private fun trackAmplitudeEvent() {
        with(viewModel.yelloDetail ?: return) {
            // TODO : 커스텀 스테이트로 분기 처리
            if (nameHint != -3) {
                AmplitudeUtils.trackEventWithProperties(EVENT_VIEW_OPEN_MESSAGE)
            }
            if (!isAnswerRevealed && nameHint == -2) {
                AmplitudeUtils.trackEventWithProperties(EVENT_VIEW_OPEN_FULL_NAME_FIRST)
            }
            if (isAnswerRevealed && nameHint == -2) {
                AmplitudeUtils.trackEventWithProperties(EVENT_VIEW_OPEN_FULL_NAME)
            }
            if (isAnswerRevealed && nameHint == -1) {
                AmplitudeUtils.trackEventWithProperties(EVENT_VIEW_OPEN_KEYWORD)
            }
            if (isAnswerRevealed && nameHint == 0 && !isSubscribe) {
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_VIEW_OPEN_FIRST_LETTER,
                    JSONObject().put(JSON_SUBSCRIPTION_TYPE, VALUE_SUB_NO),
                )
            }
            if (isAnswerRevealed && nameHint == 0 && isSubscribe) {
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_VIEW_OPEN_FIRST_LETTER,
                    JSONObject().put(JSON_SUBSCRIPTION_TYPE, VALUE_SUB_YES),
                )
            }
        }
    }

    private fun initView() {
        viewModel.getYelloDetail(id)
        viewModel.setNameIndex(nameIndex)
        viewModel.setHintUsed(isHintUsed)

        binding.tv300.paintFlags = binding.tv300.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun initClick() {
        binding.tvInitialCheck.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_OPEN_KEYWORD)
            PointUseDialog.newInstance(
                if (isKeywordOpened()) {
                    viewModel.myPoint >= COST_OPEN_FIRST_LETTER
                } else {
                    viewModel.myPoint >= COST_OPEN_KEYWORD
                },
                if (isKeywordOpened()) {
                    PointEnum.INITIAL.ordinal
                } else {
                    PointEnum.KEYWORD.ordinal
                },
            ).show(supportFragmentManager, TAG_POINT_USE_DIALOG)
        }

        binding.btnSendCheck.setOnSingleClickListener {
            if (binding.tvNameNotYet.isVisible && binding.tvKeywordNotYet.isVisible) {
                AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_OPEN_FULL_NAME_FIRST)
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_GO_SHOP,
                    JSONObject().put(JSON_SHOP_BUTTON, VALUE_CTA_NOTHING),
                )
            } else if (viewModel.yelloDetail?.isSubscribe == true && binding.tvKeywordNotYet.isGone) {
                AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_OPEN_FULL_NAME)
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_GO_SHOP,
                    JSONObject().put(JSON_SHOP_BUTTON, VALUE_CTA_KEYWORD_SUB),
                )
            } else if (viewModel.yelloDetail?.isSubscribe == false && binding.tvKeywordNotYet.isGone) {
                AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_OPEN_FULL_NAME)
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_GO_SHOP,
                    JSONObject().put(JSON_SHOP_BUTTON, VALUE_CTA_KEYWORD_NO_SUB),
                )
            } else if ((viewModel.yelloDetail?.nameHint == 0 || viewModel.yelloDetail?.nameHint == 1) && binding.tvKeywordNotYet.isVisible) {
                AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_OPEN_FULL_NAME_FIRST)
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_GO_SHOP,
                    JSONObject().put(JSON_SHOP_BUTTON, VALUE_CTA_FIRST_LETTER),
                )
            }
            Intent(this, PayActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.clSendOpen.setOnSingleClickListener {
            if (binding.tvKeywordNotYet.isVisible) {
                AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_OPEN_FULL_NAME_FIRST)
            } else {
                AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_OPEN_FULL_NAME)
            }
            ReadingTicketUseDialog.newInstance(binding.tvKeywordNotYet.isGone)
                .show(supportFragmentManager, TAG_READING_TICKET_USE_DIALOG)
        }

        binding.ivBack.setOnSingleClickListener {
            finish()
        }

        binding.btnInstagram.setOnSingleClickListener {
            if (binding.tvNameNotYet.isVisible && binding.tvKeywordNotYet.isVisible) {
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_INSTAGRAM,
                    JSONObject().put(JSON_INSTA_VIEW, VALUE_MESSAGE),
                )
            } else if (binding.tvNameNotYet.isVisible && binding.tvKeywordNotYet.isGone) {
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_INSTAGRAM,
                    JSONObject().put(JSON_INSTA_VIEW, VALUE_KEYWORD),
                )
            } else if ((viewModel.yelloDetail?.nameHint == 0 || viewModel.yelloDetail?.nameHint == 1) && binding.tvKeywordNotYet.isVisible) {
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_INSTAGRAM,
                    JSONObject().put(JSON_INSTA_VIEW, VALUE_FIRST_LETTER),
                )
            } else if (viewModel.yelloDetail?.nameHint == -2 && binding.tvKeywordNotYet.isGone) {
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_INSTAGRAM,
                    JSONObject().put(JSON_INSTA_VIEW, VALUE_FULL_NAME),
                )
            } else if (viewModel.yelloDetail?.nameHint == -2 && binding.tvKeywordNotYet.isVisible) {
                AmplitudeUtils.trackEventWithProperties(
                    EVENT_CLICK_INSTAGRAM,
                    JSONObject().put(JSON_INSTA_VIEW, VALUE_FULL_NAME_FIRST),
                )
            }
            setViewInstagram(true)
            lifecycleScope.launch {
                delay(DELAY_SHARE_STORY)
                shareInstagramStory()
            }
        }

        binding.clZeroInitialCheck.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(EVENT_CLICK_OPEN_FIRST_LETTER)
            PointUseDialog.newInstance(
                true,
                PointEnum.SUBSCRIBE.ordinal,
            ).show(supportFragmentManager, TAG_POINT_USE_DIALOG)
        }
    }

    // TODO : ENUM으로 처리 가능하도록 보완
    private fun isKeywordOpened() = binding.tvInitialCheck.text.contains("300")

    private fun observe() {
        viewModel.yelloDetailData.flowWithLifecycle(lifecycle)
            .onEach {
                binding.uiState = it.getUiStateModel()
                when (it) {
                    is UiState.Success -> {
                        binding.data = it.data
                        setData(it.data)
                    }

                    is UiState.Failure -> {
                        toast(getString(R.string.msg_auto_login_error))
                        restartApp(this)
                    }

                    else -> return@onEach
                }
            }.launchIn(lifecycleScope)
    }

    private fun setViewInstagram(isInstagram: Boolean) {
        binding.clInstagramView.visibility = if (isInstagram) View.VISIBLE else View.INVISIBLE
        binding.clTopView.visibility = if (isInstagram) View.INVISIBLE else View.VISIBLE
        binding.clBottomView.visibility = if(isInstagram) View.INVISIBLE else View.VISIBLE
    }

    private fun setData(yello: YelloDetail) {
        binding.tvSendName.isVisible = yello.nameHint != -1
        binding.tvNameNotYet.isVisible = yello.nameHint == -1
        binding.clSendOpen.isVisible = yello.ticketCount != 0
        binding.btnSendCheck.isVisible = yello.ticketCount == 0
        binding.tvKeyNumber.text = yello.ticketCount.toString()
        if (yello.nameHint >= 0) {
            binding.tvSendName.text =
                Utils.setChosungText(yello.senderName, yello.nameHint)
        } else if (yello.nameHint == -2 || yello.nameHint == -3) {
            binding.tvSendName.text = yello.senderName
        }
        binding.tvInitialCheck.isVisible = !(yello.nameHint >= 0 && yello.isAnswerRevealed)
        binding.tvGender.text =
            if (yello.senderGender.contains("FEMALE")) {
                getString(R.string.my_yello_female)
            } else {
                getString(
                    R.string.my_yello_male,
                )
            }
        binding.tvInitialCheck.text =
            if (yello.isAnswerRevealed) getString(R.string.my_yello_read_open_first_letter_with_300_points) else getString(
                R.string.my_yello_read_open_keyword_with_100_points
            )

        if (yello.isSubscribe) {
            binding.tvInitialCheck.isInvisible = yello.isAnswerRevealed
            binding.clZeroInitialCheck.isVisible = yello.isAnswerRevealed && yello.nameHint == -1
        }

        if (yello.nameHint == -2) {
            if (yello.isAnswerRevealed) {
                binding.clZeroInitialCheck.isVisible = false
                binding.tvInitialCheck.isVisible = false
            }
            binding.btnSendCheck.isVisible = false
            binding.clSendOpen.isVisible = false
        } else if (yello.nameHint == -3) {
            binding.clZeroInitialCheck.isVisible = false
            binding.tvInitialCheck.isVisible = false
            binding.clSendOpen.isVisible = false
            binding.btnSendCheck.isVisible = false
        }
        binding.tvNameCheckFinish.isVisible = yello.nameHint == -2 || yello.nameHint == -3

        if (yello.senderName == SENDER_YELLO_TEAM) {
            binding.btnSendCheck.isVisible = true
            binding.tvNameCheckFinish.isVisible = false
        }

        trackAmplitudeEvent()
    }

    private fun shareInstagramStory() {
        val backgroundAssetUri = makeUriFromView(binding.layout)
        val intent = Intent(ACTION_INSTAGRAM_ADD_TO_STORY).apply {
            setDataAndType(backgroundAssetUri, TYPE_IMAGE_JPEG)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        try {
            instagramLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            val playStoreIntent = Intent(Intent.ACTION_VIEW)
            playStoreIntent.data = Uri.parse(URI_INSTAGRAM_DOWNLOAD)
            startActivity(playStoreIntent)
        }
    }

    private fun makeUriFromView(view: View): Uri? {
        val bitmap = makeBitmapFromView(view)

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
        ActivityResultContracts.StartActivityForResult(),
    ) {
        setViewInstagram(false)
    }

    override fun finish() {
        viewModel.isHintUsed?.let {
            intent.putExtra(EXTRA_IS_HINT_USED, it)
        }
        viewModel.nameIndex?.let {
            intent.putExtra(EXTRA_NAME_INDEX, it)
        }
        intent.putExtra(EXTRA_TICKET_COUNT, viewModel.myReadingTicketCount)
        setResult(RESULT_OK, intent)
        super.finish()
    }

    companion object {
        private const val EXTRA_ID = "id"
        const val EXTRA_NAME_INDEX = "NAME_INDEX"
        const val EXTRA_IS_HINT_USED = "IS_HINT_USED"
        const val EXTRA_TICKET_COUNT = "ticketCount"

        private const val TAG_POINT_USE_DIALOG = "POINT_USE_DIALOG"
        private const val TAG_READING_TICKET_USE_DIALOG = "READING_TICKET_USE_DIALOG"

        private const val EVENT_VIEW_OPEN_MESSAGE = "view_open_message"
        private const val EVENT_VIEW_OPEN_FULL_NAME_FIRST = "view_open_fullnamefirst"
        private const val EVENT_VIEW_OPEN_FULL_NAME = "view_open_fullname"
        private const val EVENT_VIEW_OPEN_KEYWORD = "view_open_keyword"
        private const val EVENT_VIEW_OPEN_FIRST_LETTER = "view_open_firstletter"
        private const val EVENT_CLICK_OPEN_KEYWORD = "click_open_keyword"
        private const val EVENT_CLICK_OPEN_FULL_NAME_FIRST = "click_open_fullnamefirst"
        const val EVENT_CLICK_GO_SHOP = "click_go_shop"
        private const val EVENT_CLICK_OPEN_FULL_NAME = "click_open_fullname"
        private const val EVENT_CLICK_INSTAGRAM = "click_instagram"
        private const val EVENT_CLICK_OPEN_FIRST_LETTER = "click_open_firstletter"

        private const val JSON_SUBSCRIPTION_TYPE = "subscription type"
        const val JSON_SHOP_BUTTON = "shop_button"
        private const val JSON_INSTA_VIEW = "insta_view"

        private const val VALUE_SUB_NO = "sub_no"
        private const val VALUE_SUB_YES = "sub_yes"
        private const val VALUE_CTA_NOTHING = "cta_nothing"
        private const val VALUE_CTA_KEYWORD_SUB = "cta_keyword_sub"
        private const val VALUE_CTA_KEYWORD_NO_SUB = "cta_keyword_nosub"
        private const val VALUE_CTA_FIRST_LETTER = "cta_firstletter"
        private const val VALUE_MESSAGE = "message"
        private const val VALUE_KEYWORD = "keyword"
        private const val VALUE_FIRST_LETTER = "firstletter"
        private const val VALUE_FULL_NAME = "fullname"
        private const val VALUE_FULL_NAME_FIRST = "fullnamefirst"

        private const val COST_OPEN_KEYWORD = 100
        private const val COST_OPEN_FIRST_LETTER = 300

        private const val DELAY_SHARE_STORY = 500L

        private const val ACTION_INSTAGRAM_ADD_TO_STORY = "com.instagram.share.ADD_TO_STORY"

        private const val TYPE_IMAGE_JPEG = "image/jpeg"

        private const val URI_INSTAGRAM_DOWNLOAD = "market://details?id=com.instagram.android"

        const val SENDER_YELLO_TEAM = "옐로팀"

        @JvmStatic
        fun getIntent(
            context: Context,
            id: Long,
            nameIndex: Int? = null,
            isHintUsed: Boolean? = null,
        ) =
            Intent(context, MyYelloReadActivity::class.java)
                .putExtra(EXTRA_ID, id)
                .putExtra(EXTRA_NAME_INDEX, nameIndex)
                .putExtra(EXTRA_IS_HINT_USED, isHintUsed)
    }
}
