package com.example.domain.enum

enum class GradeEnum {
    A, B, C;
    override fun toString() = when (this) {
        A -> "FIRST"
        B -> "SECOND"
        C -> "THIRD"
    }
}
