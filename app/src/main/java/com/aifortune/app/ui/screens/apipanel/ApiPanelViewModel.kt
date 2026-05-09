package com.aifortune.app.ui.screens.apipanel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifortune.app.domain.model.ApiConfig
import com.aifortune.app.domain.repository.FortuneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApiPanelUiState(
    val apiConfigs: List<ApiConfig> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ApiPanelViewModel @Inject constructor(
    private val repository: FortuneRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiPanelUiState())
    val uiState: StateFlow<ApiPanelUiState> = _uiState.asStateFlow()

    init {
        loadApiConfigs()
    }

    private fun loadApiConfigs() {
        viewModelScope.launch {
            repository.getApiConfigs().collect { configs ->
                _uiState.update { it.copy(apiConfigs = configs) }
            }
        }
    }

    fun saveApiConfig(config: ApiConfig) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                repository.saveApiConfig(config)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteApiConfig(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteApiConfig(id)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun setDefaultApi(id: String) {
        viewModelScope.launch {
            try {
                val config = repository.getApiConfigById(id)
                config?.let {
                    repository.saveApiConfig(it.copy(isDefault = true))
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}
