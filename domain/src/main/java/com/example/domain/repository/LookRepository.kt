package com.example.domain.repository

import com.example.domain.entity.ResponseLookListModel

interface LookRepository {

    suspend fun getLookList(
        page: Int
    ): Result<ResponseLookListModel?>

}