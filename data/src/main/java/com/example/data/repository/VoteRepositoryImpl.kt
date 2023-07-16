package com.example.data.repository

import com.example.data.datasource.VoteDataSource
import com.example.domain.entity.Note.Friend
import com.example.domain.entity.VoteState
import com.example.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val dataSource: VoteDataSource,
) : VoteRepository {
    override suspend fun getVoteAvailable(): Result<VoteState> = kotlin.runCatching {
        dataSource.getVoteAvailable().data.toVoteState()
    }

    override suspend fun getFriendShuffle(): Result<List<Friend>> = kotlin.runCatching {
        dataSource.getFriendShuffle().data.map { friend -> friend.toFriend() }
    }
}
