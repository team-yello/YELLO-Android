package com.example.data.datasource.remote

import com.example.data.datasource.LookDataSource
import com.example.data.model.response.look.ResponseLookListDto
import com.example.data.remote.service.LookService
import javax.inject.Inject

class LookDataSourceImpl @Inject constructor(
    private val lookService: LookService
) : LookDataSource {

    override suspend fun getLookListData(
        page: Int
    ): ResponseLookListDto {
        return lookService.getLookList(page)
    }

}