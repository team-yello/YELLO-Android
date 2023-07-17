package com.example.domain.repository

import com.example.domain.entity.MyDepartment
import com.example.domain.entity.MyId
import com.example.domain.entity.MySchool
import com.example.domain.entity.RequestServiceTokenModel
import com.example.domain.entity.ServiceTokenModel

interface OnboardingRepository {

    suspend fun postTokenToServiceToken(requestServiceTokenModel: RequestServiceTokenModel): ServiceTokenModel
    suspend fun getSchoolService(
        search: String,
        page: Long,
    ): Result<MySchool>

    suspend fun getDepartmentService(
        school: String,
        search: String,
        page: Long,
    ): Result<MyDepartment>
}
