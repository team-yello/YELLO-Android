package com.example.data.repository

import com.example.data.datasource.YelloDataSource
import com.example.domain.entity.CheckKeyword
import com.example.domain.entity.CheckName
import com.example.domain.entity.FullName
import com.example.domain.entity.MyYello
import com.example.domain.entity.YelloDetail
import com.example.domain.entity.vote.VoteCount
import com.example.domain.repository.YelloRepository
import javax.inject.Inject

class YelloRepositoryImpl @Inject constructor(
    private val dataSource: YelloDataSource,
) : YelloRepository {
    override suspend fun getMyYelloList(page: Int): Result<MyYello?> {
        return runCatching { dataSource.getMyYelloList(page).data?.toTotalYello() }
    }

    override suspend fun getYelloDetail(id: Long): Result<YelloDetail?> {
        return runCatching { dataSource.getYelloDetail(id).data?.toYelloDetail() }
    }

    override suspend fun checkKeyword(id: Long): Result<CheckKeyword?> {
        return runCatching { dataSource.checkKeyword(id).data?.toCheckKeyword() }
    }

    override suspend fun checkName(id: Long): Result<CheckName?> {
        return runCatching { dataSource.checkName(id).data?.toCheckName() }
    }

    override suspend fun voteCount(): Result<VoteCount?> {
        return runCatching { dataSource.voteCount().data?.toVoteCount() }
    }

    override suspend fun postFullName(id: Long): Result<FullName?> {
        return runCatching { dataSource.postFullName(id).data?.toFullName() }
    }
}
