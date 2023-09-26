package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.datasource.paging.LookPagingSource
import com.example.data.datasource.paging.LookPagingSource.Companion.LOOK_PAGING_SIZE
import com.example.data.remote.service.LookService
import com.example.domain.entity.ResponseLookListModel.LookModel
import com.example.domain.repository.LookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LookRepositoryImpl @Inject constructor(
    private val lookService: LookService
) : LookRepository {

    override fun getLookList(): Flow<PagingData<LookModel>> {
        return Pager(
            config = PagingConfig(LOOK_PAGING_SIZE),
            pagingSourceFactory = { LookPagingSource(lookService) }
        ).flow
    }

}