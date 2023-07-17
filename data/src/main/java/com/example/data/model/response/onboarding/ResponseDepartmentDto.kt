package com.example.data.model.response.onboarding

import com.example.domain.entity.GroupList
import com.example.domain.entity.MyDepartment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDepartmentDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("groupList")
    val groupList: List<GroupListDto>,
) {
    @Serializable
    data class GroupListDto(
        @SerialName("groupId")
        val groupId: Long,
        @SerialName("departmentName")
        val departmentName: String,
    ) {
        fun toGroupList(): GroupList {
            return GroupList(groupId, departmentName)
        }
    }
    fun toMyDepartment(): MyDepartment {
        return MyDepartment(totalCount, groupList.map { it.toGroupList() })
    }
}
