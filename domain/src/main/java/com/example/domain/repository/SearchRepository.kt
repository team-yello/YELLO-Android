package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.entity.SearchListModel.SearchFriendModel
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getSearchList(
        keyword: String
    ): Flow<PagingData<SearchFriendModel>>

}