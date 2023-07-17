package com.example.data.model.response.vote

import com.example.domain.entity.vote.VoteState
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data class ResponseGetVoteAvailableDto(
    @SerialName("isPossible")
    val isStart: Boolean,
    @SerialName("point")
    val point: Int,
    @SerialName("createdAt")
    val createdAt: String,
) {
    fun toVoteState() = VoteState(
        isStart = isStart,
        point = point,
        leftTime = createdAt.toRemainTime(), // TODO: 남은 시간 변환 함수 구현
    )

    private fun String.toRemainTime(): Long {
        return try {
            val date: Date =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(this) ?: return 2400L
            val result = date.time - System.currentTimeMillis()
            Timber.tag("REMAINING TIME").d("REMAIN TIME : $result")
            result
        } catch (e: ParseException) {
            e.printStackTrace()
            2400
        }
    }
}
