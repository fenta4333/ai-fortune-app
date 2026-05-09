package com.aifortune.app.ui.screens.xueye

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifortune.app.domain.model.*
import com.aifortune.app.domain.repository.FortuneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class XueyeUiState(val isLoading: Boolean = false, val result: FortuneResult? = null, val error: String? = null)

@HiltViewModel
class XueyeViewModel @Inject constructor(private val repository: FortuneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(XueyeUiState())
    val uiState: StateFlow<XueyeUiState> = _uiState.asStateFlow()

    fun queryXueye(name: String, grade: String, targetSchool: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, result = null) }
            try {
                val configs = repository.getApiConfigs().first()
                val defaultConfig = configs.find { it.isDefault } ?: configs.firstOrNull()
                if (defaultConfig == null) { _uiState.update { it.copy(isLoading = false, error = "请先在API设置中配置大模型API") }; return@launch }
                val input = FortuneInput(name = name, birthYear = 2000, birthMonth = 1, birthDay = 1, birthHour = 0, gender = "男", type = FortuneType.XUEYE)
                repository.queryFortune(input.copy(name = "$name, 年级:$grade, 目标:$targetSchool"), defaultConfig)
                    .onSuccess { r -> _uiState.update { it.copy(isLoading = false, result = r) } }
                    .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
            } catch (e: Exception) { _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}
