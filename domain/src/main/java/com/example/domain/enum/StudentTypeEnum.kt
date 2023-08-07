package com.example.domain.enum

enum class StudentTypeEnum {
    H, U;
    override fun toString() = when (this) {
        H -> "HIGHSCHOOL"
        U -> "UNIVERSITY"
    }
}
