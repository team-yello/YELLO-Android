package com.example.domain.entity.onboarding

data class GroupHighSchoolList(
    val totalCount: Int,
    val groupList: List<Group>,
)

data class GroupHighSchool(
    val groupId: Long,
    val className: String,
)
