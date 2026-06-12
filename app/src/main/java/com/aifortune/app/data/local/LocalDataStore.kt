package com.aifortune.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aifortune.app.domain.model.ApiConfig
import com.aifortune.app.domain.model.HistoryItem
import com.aifortune.app.ui.theme.ThemeConfig
import com.aifortune.app.ui.theme.ThemeVariant
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

        // Theme preferences
        private val THEME_VARIANT_KEY = stringPreferencesKey("theme_variant")
        private val THEME_IS_DARK_KEY = stringPreferencesKey("theme_is_dark")
        private val BG_IMAGE_PATH_KEY = stringPreferencesKey("bg_image_path")
        private val BG_BLUR_KEY = floatPreferencesKey("bg_blur_radius")
        private val BG_DIM_KEY = floatPreferencesKey("bg_dim_alpha")
        private val USE_GRADIENT_KEY = stringPreferencesKey("use_gradient_bg")
    }

    // ---- First Launch ----
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { prefs ->
        val firstLaunch = prefs[FIRST_LAUNCH_KEY]
        firstLaunch == null || firstLaunch == "true"
    }

    suspend fun setFirstLaunchComplete() {
        context.dataStore.edit { prefs ->
            prefs[FIRST_LAUNCH_KEY] = "false"
        }
    }

    // ---- Theme Config (type-safe one-shot) ----
    suspend fun getThemeConfig(): ThemeConfig {
        val prefs = context.dataStore.data.first()
        return ThemeConfig(
            variant = try {
                ThemeVariant.valueOf(prefs[THEME_VARIANT_KEY] ?: "DEEP_INK")
            } catch (_: Exception) { ThemeVariant.DEEP_INK },
            isDark = prefs[THEME_IS_DARK_KEY]?.toBoolean() ?: true,
            backgroundImagePath = prefs[BG_IMAGE_PATH_KEY],
            blurRadius = prefs[BG_BLUR_KEY] ?: 0f,
            dimAlpha = prefs[BG_DIM_KEY] ?: 0.4f,
            useGradientBackground = prefs[USE_GRADIENT_KEY]?.toBoolean() ?: true
        )
    }

    val themeConfigFlow: Flow<ThemeConfig> = context.dataStore.data.map { prefs ->
        ThemeConfig(
            variant = try {
                ThemeVariant.valueOf(prefs[THEME_VARIANT_KEY] ?: "DEEP_INK")
            } catch (_: Exception) { ThemeVariant.DEEP_INK },
            isDark = prefs[THEME_IS_DARK_KEY]?.toBoolean() ?: true,
            backgroundImagePath = prefs[BG_IMAGE_PATH_KEY],
            blurRadius = prefs[BG_BLUR_KEY] ?: 0f,
            dimAlpha = prefs[BG_DIM_KEY] ?: 0.4f,
            useGradientBackground = prefs[USE_GRADIENT_KEY]?.toBoolean() ?: true
        )
    }

    suspend fun saveThemeConfig(config: ThemeConfig) {
        context.dataStore.edit { prefs ->
            prefs[THEME_VARIANT_KEY] = config.variant.name
            prefs[THEME_IS_DARK_KEY] = config.isDark.toString()
            prefs[BG_IMAGE_PATH_KEY] = config.backgroundImagePath ?: ""
            prefs[BG_BLUR_KEY] = config.blurRadius
            prefs[BG_DIM_KEY] = config.dimAlpha
            prefs[USE_GRADIENT_KEY] = config.useGradientBackground.toString()
        }
    }

    // ---- API Configs ----
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
        return try {
            val prefs = context.dataStore.data.first()
            val currentJson = prefs[API_CONFIGS_KEY] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<ApiConfig>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            currentList.find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }

    // ---- History ----
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
            currentList.add(0, item)
            prefs[HISTORY_KEY] = json.encodeToString(currentList.take(100))
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