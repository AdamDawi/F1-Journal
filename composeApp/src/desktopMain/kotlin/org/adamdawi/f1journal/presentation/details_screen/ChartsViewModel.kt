package org.adamdawi.f1journal.presentation.details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.adamdawi.f1journal.domain.DriverPerformanceDifference
import org.adamdawi.f1journal.domain.F1Repository
import org.adamdawi.f1journal.domain.util.Result

class ChartsViewModel(
    private val repository: F1Repository
): ViewModel() {
    private val _state = MutableStateFlow(ChartsState())
    val state = _state.onStart {
        getDrivers()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =_state.value
    )

    private fun getDrivers(){
        when(val result = repository.getDrivers()){
            is Result.Error -> {

            }
            is Result.Success -> {
                _state.update {
                    it.copy(drivers = result.data)
                }
                val performanceDifferences = result.data.map { driver ->
                    val difference = driver.dryAvgPosition - driver.rainyAvgPosition
                    DriverPerformanceDifference(driver.driverName, difference)
                }
                // Sorting the list to show who has the biggest positive difference (i.e., improvement in the rain)
                val sortedPerformanceDifferences = performanceDifferences.sortedByDescending { it.performanceDifference }
                _state.update {
                    it.copy(driversDifference = sortedPerformanceDifferences)
                }
            }
        }
    }
}