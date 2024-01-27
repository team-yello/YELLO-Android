package com.example.domain.enum

enum class SubscribeType {
    CANCELED,
    NORMAL,
    ACTIVE,
    ERROR,
    ;

    override fun toString() = when (this) {
        CANCELED -> "canceled"
        NORMAL -> "normal"
        ACTIVE -> "active"
        ERROR -> "error"
    }
}

fun String.toSubscribeType(): SubscribeType {
    return when (this) {
        "canceled" -> SubscribeType.CANCELED
        "normal" -> SubscribeType.NORMAL
        "active" -> SubscribeType.ACTIVE
        else -> SubscribeType.ERROR
    }
}
