package com.aifortune.app

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
class MainViewModel @Inject constructor(
    private val localDataStore: LocalDataStore
) : ViewModel() {

    private val _isFirstLaunch = MutableStateFlow(true)
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    init {
        viewModelScope.launch {
            localDataStore.isFirstLaunch.collect { isFirst ->
                _isFirstLaunch.value = isFirst
            }
        }
    }

    fun completeFirstLaunch() {
        viewModelScope.launch {
            localDataStore.setFirstLaunchComplete()
        }
    }
}
