package com.example.domain.enums

enum class Gender {
    M, W;

    override fun toString() = when (this) {
        M -> "MALE"
        W -> "FEMALE"
    }
}
