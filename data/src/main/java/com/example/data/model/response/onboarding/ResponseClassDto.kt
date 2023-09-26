package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.GroupHighSchool
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseClassDto(
    @SerialName("groupId")
    val groupId: Long,
) {
    fun toGroupHighSchool(): GroupHighSchool {
        return GroupHighSchool(groupId)
    }
}



