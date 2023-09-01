package com.example.domain.repository

import com.example.domain.entity.vote.ChoiceList
import com.example.domain.entity.vote.Note
import com.example.domain.entity.vote.Note.Friend
import com.example.domain.entity.vote.StoredVote
import com.example.domain.entity.vote.VoteState

interface VoteRepository {
    suspend fun getVoteAvailable(): Result<VoteState?>

    suspend fun getFriendShuffle(): Result<List<Friend>?>

    suspend fun getVoteQuestion(): Result<List<Note>?>

    suspend fun postVote(choiceList: ChoiceList): Result<Int?>

    fun getStoredVote(): StoredVote?

    fun setStoredVote(storedVote: StoredVote)

    fun clearStoredVote()
}
