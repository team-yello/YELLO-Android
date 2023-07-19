package com.example.domain.enum

enum class GenderEnum {
    M, W;

    override fun toString() = when (this) {
        M -> "MALE"
        W -> "FEMALE"
    }
}
