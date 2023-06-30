package com.example.domain.entity

data class MyYello(
    val yelloCount: Int,
    val yello: List<Yello>
)

data class Yello(
    val id: Int,
    val sex: String,
    val createdAt: String
)