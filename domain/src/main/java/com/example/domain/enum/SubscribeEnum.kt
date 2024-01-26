package com.example.domain.enum

enum class SubscribeEnum {
    CANCELED,
    NORMAL,
    ACTIVE, ;
    override fun toString() = when (this) {
        CANCELED -> "canceled"
        NORMAL -> "normal"
        ACTIVE -> "active"
    }
}
