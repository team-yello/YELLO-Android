package com.example.domain.enums

enum class Grade {
    First, Second, Third;

    fun toInt() = when (this) {
        First -> 1
        Second -> 2
        Third -> 3
    }
}
