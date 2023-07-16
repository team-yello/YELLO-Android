package com.example.data.repository

import com.example.data.datasource.VoteDataSource
import com.example.data.model.request.toRequestPostVoteDto
import com.example.domain.entity.vote.ChoiceList
import com.example.domain.entity.vote.Note
import com.example.domain.entity.vote.Note.Friend
import com.example.domain.entity.vote.Point
import com.example.domain.entity.vote.VoteState
import com.example.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val dataSource: VoteDataSource,
) : VoteRepository {
    override suspend fun getVoteAvailable(): Result<VoteState?> = runCatching {
        dataSource.getVoteAvailable().data?.toVoteState()
    }

    override suspend fun getFriendShuffle(): Result<List<Friend>?> = runCatching {
        dataSource.getFriendShuffle().data?.map { friend -> friend.toFriend() }
    }

    override suspend fun getVoteQuestion(): Result<List<Note>?> = runCatching {
        dataSource.getVoteQuestion().data?.map { question -> question.toNote() }
    }

    override suspend fun postVote(choiceList: ChoiceList): Result<Point?> = runCatching {
        dataSource.postVote(choiceList.toRequestPostVoteDto()).data?.toPoint()
    }
}
