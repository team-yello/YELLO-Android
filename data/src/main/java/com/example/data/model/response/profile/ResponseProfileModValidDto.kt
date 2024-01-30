package com.example.data.model.response.profile

import com.example.domain.entity.ProfileModValidModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileModValidDto(
    @SerialName("tag")
    val tag: String,
    @SerialName("value")
    val value: String
) {
    fun toProfileModValidModel(): ProfileModValidModel {
        return ProfileModValidModel(
            tag, value
        )

    }
}
