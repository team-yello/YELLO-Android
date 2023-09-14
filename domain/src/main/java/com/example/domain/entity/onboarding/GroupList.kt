package com.example.domain.entity.onboarding

import kotlin.String

data class GroupList(
    val totalCount: Int,
    val groupList: List<Group>,
)

data class Group(
    val groupId: Long,
    val name: String,
)