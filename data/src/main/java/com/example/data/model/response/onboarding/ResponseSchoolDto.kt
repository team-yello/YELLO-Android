package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.MySchool
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSchoolDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("groupNameList")
    val groupNameList: List<String>,
) {
    fun toMySchool(): MySchool {
        return MySchool(totalCount, groupNameList)
    }
}
