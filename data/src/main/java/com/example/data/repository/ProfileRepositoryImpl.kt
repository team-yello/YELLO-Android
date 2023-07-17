package com.example.data.repository

import com.example.data.datasource.ProfileDataSource
import com.example.domain.entity.ProfileUserModel
import com.example.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource
) : ProfileRepository {

    override suspend fun getUserData(userId: Int): ProfileUserModel {
        return profileDataSource.getUserData(userId).data.toProfileUserModel()
    }

}