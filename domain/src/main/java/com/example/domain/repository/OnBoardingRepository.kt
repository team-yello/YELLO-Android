package com.example.domain.repository

import com.example.domain.entity.ServiceTokenModel

interface OnBoardingRepository {

    suspend fun getServiceToken(): ServiceTokenModel

}