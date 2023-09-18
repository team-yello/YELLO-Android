package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.HighSchoolList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseHighSchoolDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("groupNameList")
    val groupNameList: List<String>,
) {
    fun toMyHighSchool(): HighSchoolList {
        return HighSchoolList(
            totalCount = totalCount,
            groupNameList = groupNameList,
        )
    }
}

// TODO :고등학교로 바꾸기 dto
