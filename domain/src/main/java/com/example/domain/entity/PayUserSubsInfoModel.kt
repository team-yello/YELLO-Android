package com.example.domain.entity

import com.example.domain.enums.SubscribeType

class PayUserSubsInfoModel(
    val id: Long,
    val subscribe: SubscribeType,
    val expiredDate: String,
)
