package com.example.domain.entity.onboarding

data class MyDepartment(
    val totalCount: Int,
    val groupList: List<GroupList>,
)
data class GroupList(
    val groupId: Long,
    val departmentName: String,
)
