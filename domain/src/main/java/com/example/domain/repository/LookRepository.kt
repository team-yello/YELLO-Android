package com.example.domain.repository

import com.example.domain.entity.LookListModel

interface LookRepository {

    suspend fun getLookList(
        page: Int
    ): LookListModel?

}