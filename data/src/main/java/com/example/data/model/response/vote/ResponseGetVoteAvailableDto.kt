package com.example.data.model.response.vote

import com.example.domain.entity.vote.VoteState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        leftTime = createdAt.toRemainTime(),
    )

    private fun String.toRemainTime(): Long {
        return try {
            val date: Date =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(this) ?: return 0L
            val result = date.time - System.currentTimeMillis()
            Timber.d("VOTE REMAINING TIME : ${result / 100}")
            result / 100
        } catch (e: ParseException) {
            e.printStackTrace()
            2400
        }
    }
}
