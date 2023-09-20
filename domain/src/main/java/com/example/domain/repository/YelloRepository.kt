package com.example.domain.repository

import com.example.domain.entity.CheckKeyword
import com.example.domain.entity.CheckName
import com.example.domain.entity.FullName
import com.example.domain.entity.MyYello
import com.example.domain.entity.YelloDetail
import com.example.domain.entity.vote.VoteCount

interface YelloRepository {
    suspend fun getMyYelloList(page: Int): Result<MyYello?>
    suspend fun getYelloDetail(id: Long): Result<YelloDetail?>
    suspend fun checkKeyword(id: Long): Result<CheckKeyword?>
    suspend fun checkName(id: Long): Result<CheckName?>
    suspend fun voteCount(): Result<VoteCount?>
    suspend fun postFullName(id: Long): Result<FullName?>
}
