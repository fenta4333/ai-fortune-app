package com.aifortune.app.ui.screens.bazi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifortune.app.domain.model.*
import com.aifortune.app.domain.repository.FortuneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BaziUiState(
    val isLoading: Boolean = false,
    val result: FortuneResult? = null,
    val error: String? = null,
    val hasApiConfig: Boolean = false
)

@HiltViewModel
class BaziViewModel @Inject constructor(
    private val repository: FortuneRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BaziUiState())
    val uiState: StateFlow<BaziUiState> = _uiState.asStateFlow()

    init {
        checkApiConfig()
    }

    private fun checkApiConfig() {
        viewModelScope.launch {
            repository.getApiConfigs().collect { configs ->
                _uiState.update { it.copy(hasApiConfig = configs.isNotEmpty()) }
            }
        }
    }

    fun queryBazi(
        name: String,
        birthYear: Int,
        birthMonth: Int,
        birthDay: Int,
        birthHour: Int,
        gender: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, result = null) }

            try {
                val configs = repository.getApiConfigs().first()
                val defaultConfig = configs.find { it.isDefault } ?: configs.firstOrNull()

                if (defaultConfig == null) {
                    _uiState.update { 
                        it.copy(isLoading = false, error = "请先在API设置中配置大模型API") 
                    }
                    return@launch
                }

                val input = FortuneInput(
                    name = name,
                    birthYear = birthYear,
                    birthMonth = birthMonth,
                    birthDay = birthDay,
                    birthHour = birthHour,
                    gender = gender,
                    type = FortuneType.BAZI
                )

                repository.queryFortune(input, defaultConfig)
                    .onSuccess { result ->
                        _uiState.update { it.copy(isLoading = false, result = result) }
                    }
                    .onFailure { e ->
                        _uiState.update { it.copy(isLoading = false, error = e.message) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
