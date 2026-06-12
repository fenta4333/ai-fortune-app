package com.aifortune.app.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifortune.app.data.local.LocalDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val dataStore: LocalDataStore
) : ViewModel() {

    private val _themeConfig = MutableStateFlow(ThemeConfig())
    val themeConfig: StateFlow<ThemeConfig> = _themeConfig.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.themeConfigFlow.collect { config ->
                _themeConfig.value = config
            }
        }
    }

    fun updateVariant(variant: ThemeVariant) {
        viewModelScope.launch {
            val updated = _themeConfig.value.copy(variant = variant)
            _themeConfig.value = updated
            dataStore.saveThemeConfig(updated)
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            val updated = _themeConfig.value.copy(isDark = !_themeConfig.value.isDark)
            _themeConfig.value = updated
            dataStore.saveThemeConfig(updated)
        }
    }

    fun setBackgroundImagePath(path: String?) {
        viewModelScope.launch {
            val updated = _themeConfig.value.copy(backgroundImagePath = path)
            _themeConfig.value = updated
            dataStore.saveThemeConfig(updated)
        }
    }

    fun setBlurRadius(radius: Float) {
        viewModelScope.launch {
            val updated = _themeConfig.value.copy(blurRadius = radius)
            _themeConfig.value = updated
            dataStore.saveThemeConfig(updated)
        }
    }

    fun setDimAlpha(alpha: Float) {
        viewModelScope.launch {
            val updated = _themeConfig.value.copy(dimAlpha = alpha)
            _themeConfig.value = updated
            dataStore.saveThemeConfig(updated)
        }
    }

    fun setUseGradientBackground(use: Boolean) {
        viewModelScope.launch {
            val updated = _themeConfig.value.copy(useGradientBackground = use)
            _themeConfig.value = updated
            dataStore.saveThemeConfig(updated)
        }
    }

    fun clearBackground() {
        setBackgroundImagePath(null)
    }
}