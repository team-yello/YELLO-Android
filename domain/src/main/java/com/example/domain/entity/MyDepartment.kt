package com.example.domain.entity

data class MyDepartment(
    val totalCount: Int,
    val groupList: List<GroupList>,
)
data class GroupList(
    val groupId: Long,
    val departmentName: String,
)
