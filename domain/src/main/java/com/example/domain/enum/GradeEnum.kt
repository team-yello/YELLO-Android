package com.example.domain.enum

enum class GradeEnum {
    A, B, C;
    override fun toString() = when (this) {
        A -> "1학년"
        B -> "2학년"
        C -> "3학년"
    }
}
