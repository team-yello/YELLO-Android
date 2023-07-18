package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.Group
import com.example.domain.entity.onboarding.GroupList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDepartmentDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("group")
    val groupList: List<GroupListDto>,
) {
    @Serializable
    data class GroupListDto(
        @SerialName("groupId")
        val groupId: Long,
        @SerialName("departmentName")
        val departmentName: String,
    ) {
        fun toGroupList(): Group {
            return Group(groupId, departmentName)
        }
    }

    fun toMyDepartment(): GroupList {
        return GroupList(totalCount, groupList.map { it.toGroupList() })
    }
}
