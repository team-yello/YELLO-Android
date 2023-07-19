package com.example.domain.repository

import com.example.domain.entity.CheckKeyword
import com.example.domain.entity.CheckName
import com.example.domain.entity.MyYello
import com.example.domain.entity.Response
import com.example.domain.entity.YelloDetail

interface YelloRepository {
    suspend fun getMyYelloList(page: Int): Result<MyYello?>
    suspend fun getYelloDetail(id: Long): Result<YelloDetail?>
    suspend fun checkKeyword(id: Long): Result<CheckKeyword?>
    suspend fun checkName(id: Long): Result<CheckName?>
    suspend fun payCheck(index: Int): Result<Response>
}
