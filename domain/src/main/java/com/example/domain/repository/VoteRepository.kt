package com.example.domain.repository

import com.example.domain.entity.VoteState

interface VoteRepository {
    suspend fun getVoteAvailable(): Result<VoteState>
}
