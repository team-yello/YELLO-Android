package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.GroupHighSchool
import com.example.domain.entity.onboarding.GroupHighSchoolList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class ResponseClassDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("groupList")
    val groupList: List<GroupListDto>,
) {
    @Serializable
    data class GroupListDto(
        @SerialName("groupId")
        val groupId: Long,
        @SerialName("grade")
        val grade: String,
        @SerialName("className")
        val className: String,
    ) {
        fun toGroupHighSchoolList(): GroupHighSchool {
            return GroupHighSchool(groupId, grade, className)
        }
    }

    fun toMyClass(): GroupHighSchoolList {
        return GroupHighSchoolList(totalCount, groupList.map { it.toGroupHighSchoolList() })
    }
}
