package com.example.domain.enum

enum class StudentType {
    HIGHSCHOOL, UNIVERSITY;
    override fun toString() = when (this) {
        HIGHSCHOOL -> "HIGHSCHOOL"
        UNIVERSITY -> "UNIVERSITY"
    }
}
