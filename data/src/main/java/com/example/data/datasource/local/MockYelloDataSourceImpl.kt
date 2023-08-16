package com.example.data.datasource.local

import com.example.data.datasource.YelloDataSource
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.ResponseDto
import com.example.data.model.response.vote.ResponseVoteCount
import com.example.data.model.response.yello.ResponseCheckKeyword
import com.example.data.model.response.yello.ResponseCheckName
import com.example.data.model.response.yello.ResponseMyYello
import com.example.data.model.response.yello.ResponseYelloDetail
import com.example.data.util.FileParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class MockYelloDataSourceImpl @Inject constructor(
    private val parser: FileParser,
) : YelloDataSource {
    override suspend fun getMyYelloList(page: Int): BaseResponse<ResponseMyYello> {
        val file = withContext(Dispatchers.IO) {
            runCatching { parser.execute("fake_total_yello.json") }
                .onFailure(Timber::e)
                .getOrNull()
        }
        return Json.decodeFromString(file ?: throw IllegalArgumentException("File State Error"))
    }

    override suspend fun getYelloDetail(id: Long): BaseResponse<ResponseYelloDetail> {
        val file = withContext(Dispatchers.IO) {
            runCatching { parser.execute("fake_detail_yello.json") }
                .onFailure(Timber::e)
                .getOrNull()
        }
        return Json.decodeFromString(file ?: throw IllegalArgumentException("File State Error"))
    }

    override suspend fun checkKeyword(id: Long): BaseResponse<ResponseCheckKeyword> {
        val file = withContext(Dispatchers.IO) {
            runCatching { parser.execute("fake_check_keyword.json") }
                .onFailure(Timber::e)
                .getOrNull()
        }
        return Json.decodeFromString(file ?: throw IllegalArgumentException("File State Error"))
    }

    override suspend fun checkName(id: Long): BaseResponse<ResponseCheckName> {
        val file = withContext(Dispatchers.IO) {
            runCatching { parser.execute("fake_check_name.json") }
                .onFailure(Timber::e)
                .getOrNull()
        }
        return Json.decodeFromString(file ?: throw IllegalArgumentException("File State Error"))
    }

    override suspend fun payCheck(index: Int): ResponseDto {
        TODO("Not yet implemented")
    }

    override suspend fun voteCount(): BaseResponse<ResponseVoteCount> {
        TODO("Not yet implemented")
    }
}
