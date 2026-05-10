package com.aifortune.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aifortune.app.domain.model.ApiConfig
import com.aifortune.app.domain.model.HistoryItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ai_fortune_prefs")

@Singleton
class LocalDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private val API_CONFIGS_KEY = stringPreferencesKey("api_configs")
        private val DEFAULT_API_ID_KEY = stringPreferencesKey("default_api_id")
        private val HISTORY_KEY = stringPreferencesKey("fortune_history")
        private val FIRST_LAUNCH_KEY = stringPreferencesKey("first_launch")
    }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { prefs ->
        val firstLaunch = prefs[FIRST_LAUNCH_KEY]
        firstLaunch == null || firstLaunch == "true"
    }

    suspend fun setFirstLaunchComplete() {
        context.dataStore.edit { prefs ->
            prefs[FIRST_LAUNCH_KEY] = "false"
        }
    }

    val apiConfigs: Flow<List<ApiConfig>> = context.dataStore.data.map { prefs ->
        val configsJson = prefs[API_CONFIGS_KEY] ?: "[]"
        try {
            json.decodeFromString<List<ApiConfig>>(configsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    val defaultApiId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[DEFAULT_API_ID_KEY]
    }

    suspend fun saveApiConfig(config: ApiConfig) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[API_CONFIGS_KEY] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<ApiConfig>>(currentJson).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
            
            val existingIndex = currentList.indexOfFirst { it.id == config.id }
            if (existingIndex >= 0) {
                currentList[existingIndex] = config
            } else {
                currentList.add(config.copy(id = System.currentTimeMillis().toString()))
            }
            
            // If this is set as default, unset others
            if (config.isDefault) {
                currentList.forEachIndexed { index, c ->
                    if (c.id != config.id) {
                        currentList[index] = c.copy(isDefault = false)
                    }
                }
            }
            
            prefs[API_CONFIGS_KEY] = json.encodeToString(currentList)
            
            if (config.isDefault) {
                prefs[DEFAULT_API_ID_KEY] = config.id
            }
        }
    }

    suspend fun deleteApiConfig(configId: String) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[API_CONFIGS_KEY] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<ApiConfig>>(currentJson).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
            currentList.removeAll { it.id == configId }
            prefs[API_CONFIGS_KEY] = json.encodeToString(currentList)
        }
    }

    suspend fun getApiConfigById(id: String): ApiConfig? {
        var result: ApiConfig? = null
        context.dataStore.data.collect { prefs ->
            val currentJson = prefs[API_CONFIGS_KEY] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<ApiConfig>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            result = currentList.find { it.id == id }
        }
        return result
    }

    // History operations
    val historyItems: Flow<List<HistoryItem>> = context.dataStore.data.map { prefs ->
        val historyJson = prefs[HISTORY_KEY] ?: "[]"
        try {
            json.decodeFromString<List<HistoryItem>>(historyJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addHistoryItem(item: HistoryItem) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[HISTORY_KEY] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<HistoryItem>>(currentJson).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
            // Add new item at the beginning
            currentList.add(0, item)
            // Keep only last 100 items
            val trimmedList = currentList.take(100)
            prefs[HISTORY_KEY] = json.encodeToString(trimmedList)
        }
    }

    suspend fun deleteHistoryItem(itemId: String) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[HISTORY_KEY] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<HistoryItem>>(currentJson).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
            currentList.removeAll { it.id == itemId }
            prefs[HISTORY_KEY] = json.encodeToString(currentList)
        }
    }

    suspend fun clearHistory() {
        context.dataStore.edit { prefs ->
            prefs[HISTORY_KEY] = "[]"
        }
    }
}
