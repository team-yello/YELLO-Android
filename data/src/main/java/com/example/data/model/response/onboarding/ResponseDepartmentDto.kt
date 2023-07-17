package com.example.data.model.response.onboarding

import kotlinx.serialization.SerialName
import java.util.Objects

data class ResponseDepartmentDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("groupList")
    val groupList: Objects,
    @SerialName("groupId")
    val groupId: Long,
    @SerialName("deaprtmentName")
    val deaprtmentName: String,
)
