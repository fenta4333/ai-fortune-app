package com.aifortune.app.domain.repository

import com.aifortune.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface FortuneRepository {
    fun getApiConfigs(): Flow<List<ApiConfig>>
    fun getDefaultApiId(): Flow<String?>
    suspend fun saveApiConfig(config: ApiConfig)
    suspend fun deleteApiConfig(id: String)
    suspend fun getApiConfigById(id: String): ApiConfig?
    suspend fun queryFortune(input: FortuneInput, apiConfig: ApiConfig?): Result<FortuneResult>
}
