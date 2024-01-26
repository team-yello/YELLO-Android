package com.example.domain.enum

enum class Grade {
    A, B, C;

    fun toInt() = when (this) {
        A -> 1
        B -> 2
        C -> 3
    }
}
