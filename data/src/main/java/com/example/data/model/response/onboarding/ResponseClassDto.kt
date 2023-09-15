package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.Group
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
        @SerialName("className")
        val className: String,
    ) {
        fun toGroupHighSchoolList(): Group {
            return Group(groupId, className)
        }
    }

    fun toMyClass(): GroupHighSchoolList {
        return GroupHighSchoolList(totalCount, groupList.map { it.toGroupHighSchoolList() })
    }
}
