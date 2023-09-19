package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.entity.ResponseLookListModel.LookModel
import kotlinx.coroutines.flow.Flow

interface LookRepository {

    suspend fun getLookList(): Flow<PagingData<LookModel>>

}