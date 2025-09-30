package com.example.sports.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.repository.SportsPerformanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddPerformanceViewModel(
    private val repository: SportsPerformanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPerformanceUiState())
    val uiState: StateFlow<AddPerformanceUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }

    fun updateDuration(duration: String) {
        val durationInt = duration.toIntOrNull() ?: 0
        _uiState.value = _uiState.value.copy(duration = duration, durationInt = durationInt)
    }

    fun updateStorageType(storageType: StorageType) {
        _uiState.value = _uiState.value.copy(storageType = storageType)
    }

    fun savePerformance(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.name.isBlank() || state.location.isBlank() || state.durationInt <= 0) {
            _uiState.value = state.copy(errorMessage = "Please fill all fields correctly")
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val performance = SportsPerformance(
                    name = state.name.trim(),
                    location = state.location.trim(),
                    duration = state.durationInt,
                    storageType = state.storageType
                )

                // check insert result for better error handling
                repository.insertPerformance(performance)
                //not needed
                _uiState.value = AddPerformanceUiState()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = "Failed to save performance: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class AddPerformanceUiState(
    val name: String = "",
    val location: String = "",
    val duration: String = "",
    val durationInt: Int = 0,
    val storageType: StorageType = StorageType.LOCAL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)