package com.example.domain.enum

enum class Gender {
    M, W;

    override fun toString() = when (this) {
        M -> "MALE"
        W -> "FEMALE"
    }
}
