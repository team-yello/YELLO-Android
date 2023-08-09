package com.example.data.repository

import com.example.data.datasource.LookDataSource
import com.example.domain.entity.LookListModel
import com.example.domain.repository.LookRepository
import javax.inject.Inject

class LookRepositoryImpl @Inject constructor(
    private val lookDataSource: LookDataSource,
) : LookRepository {

    override suspend fun getLookList(
        page: Int
    ): LookListModel {
        return lookDataSource.getLookListData(
            page
        ).toLookListModel()
    }

}