package com.example.domain.repository

import com.example.domain.entity.SearchListModel

interface SearchRepository {

    suspend fun getSearchList(
        page: Int,
        keyword: String
    ): Result<SearchListModel?>

}