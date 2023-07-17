package com.example.data.datasource

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.profile.ResponseProfileUserDto

interface ProfileDataSource {

    suspend fun getUserData(
        userId: Int
    ): BaseResponse<ResponseProfileUserDto>

}