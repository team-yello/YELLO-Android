package com.example.domain.entity.onboarding

data class GroupHighSchoolList(
    val totalCount: Int,
    val groupList: List<GroupHighSchool>,
)

data class GroupHighSchool(
    val groupId: Long,
    val grade: String,
    val className: String,
)
