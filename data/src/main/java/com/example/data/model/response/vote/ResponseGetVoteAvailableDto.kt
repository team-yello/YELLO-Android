package com.example.data.model.response.vote

import com.example.domain.entity.vote.VoteState
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.TimeZone
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
        leftTime = createdAt.toRemainTime(),
    )

    private fun String.toRemainTime(): Long {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            val currentTime = System.currentTimeMillis()
            val targetDateTime = dateFormat.parse(this)?.time ?: return 0
            val timeDifference = targetDateTime - currentTime

            Timber.d("REMAINING TIME : ${timeDifference / 1000}")

            timeDifference / 1000
        } catch (e: ParseException) {
            e.printStackTrace()
            2400
        }
    }
}
