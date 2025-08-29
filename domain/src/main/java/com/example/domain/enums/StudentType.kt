package com.example.domain.enums

enum class StudentType {
    SCHOOL, UNIVERSITY;
    override fun toString() = when (this) {
        SCHOOL -> "SCHOOL"
        UNIVERSITY -> "UNIVERSITY"
    }
}
