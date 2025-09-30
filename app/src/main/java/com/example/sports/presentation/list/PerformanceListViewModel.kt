package com.example.sports.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.repository.SportsPerformanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PerformanceListViewModel(
    private val repository: SportsPerformanceRepository
) : ViewModel() {
    // Contract, UiState, UiData, UiAction, UiEvent, StateFlowViewModel
    private val _uiState = MutableStateFlow(PerformanceListUiState())
    val uiState: StateFlow<PerformanceListUiState> = _uiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow(FilterType.ALL)

    init {
        loadPerformances()
    }

    private fun loadPerformances() {
        viewModelScope.launch {
            combine(
                repository.getAllPerformances(),
                _selectedFilter
            ) { performances, filter ->
                val filteredPerformances = when (filter) {
                    FilterType.ALL -> performances
                    FilterType.LOCAL -> performances.filter { it.storageType == StorageType.LOCAL }
                    FilterType.REMOTE -> performances.filter { it.storageType == StorageType.REMOTE }
                }

                // uiState.update{copy xy}
                _uiState.value = _uiState.value.copy(
                    performances = filteredPerformances,
                    selectedFilter = filter,
                    isLoading = false
                )
            }.collect {
            // update ui state in collect
            }
        }
    }

    fun refreshData() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadPerformances()
    }

    fun updateFilter(filter: FilterType) {
        _selectedFilter.value = filter
    }

    fun deletePerformance(performance: SportsPerformance) {
        viewModelScope.launch {
            //runCatching, success/failure - update ui state
            try {
                repository.deletePerformance(performance)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to delete performance: ${e.message}")
            }
        }
    }

}

// move to separate classes
// Contract, UiState, UiData, UiAction, UiEvent, StateFlowViewModel
data class PerformanceListUiState(
    val performances: List<SportsPerformance> = emptyList(),
    val selectedFilter: FilterType = FilterType.ALL,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

enum class FilterType {
    ALL,
    LOCAL,
    REMOTE
}