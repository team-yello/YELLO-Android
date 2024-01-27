package com.example.domain.enum

enum class StudentType {
    H, U;
    override fun toString() = when (this) {
        H -> "HIGHSCHOOL"
        U -> "UNIVERSITY"
    }
}
