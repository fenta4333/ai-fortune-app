package com.aifortune.app.ui.screens.name

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifortune.app.domain.model.*
import com.aifortune.app.domain.repository.FortuneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NameUiState(val isLoading: Boolean = false, val result: FortuneResult? = null, val error: String? = null)

@HiltViewModel
class NameViewModel @Inject constructor(private val repository: FortuneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NameUiState())
    val uiState: StateFlow<NameUiState> = _uiState.asStateFlow()

    fun queryName(name: String, birthYear: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, result = null) }
            try {
                val configs = repository.getApiConfigs().first()
                val defaultConfig = configs.find { it.isDefault } ?: configs.firstOrNull()
                if (defaultConfig == null) { _uiState.update { it.copy(isLoading = false, error = "请先在API设置中配置大模型API") }; return@launch }
                val year = birthYear.toIntOrNull() ?: 2000
                val input = FortuneInput(name = name, birthYear = year, birthMonth = 1, birthDay = 1, birthHour = 0, gender = "男", type = FortuneType.NAME)
                repository.queryFortune(input, defaultConfig)
                    .onSuccess { r ->
                        repository.addHistoryItem(HistoryItem(type = r.type, title = r.title, content = r.content, input = "$name, ${birthYear}年"))
                        _uiState.update { it.copy(isLoading = false, result = r) }
                    }
                    .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
            } catch (e: Exception) { _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}
