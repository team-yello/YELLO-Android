package com.example.domain.repository

import com.example.domain.entity.ProfileUserModel

interface ProfileRepository {

    suspend fun getUserData(
        userId: String
    ): ProfileUserModel

}