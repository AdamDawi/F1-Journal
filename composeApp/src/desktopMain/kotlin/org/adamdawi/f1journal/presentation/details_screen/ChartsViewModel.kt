package org.adamdawi.f1journal.presentation.details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adamdawi.f1journal.domain.model.DriverPerformanceDifference
import org.adamdawi.f1journal.domain.repository.F1Repository
import org.adamdawi.f1journal.domain.util.Result

class ChartsViewModel(
    private val repository: F1Repository
): ViewModel() {
    private val _state = MutableStateFlow(ChartsState())
    val state = _state.onStart {
        _state.update {
            it.copy(isLoading = true)
        }
        getDrivers()
        getTemperatureLapTime()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =_state.value
    )
    private var loadingCounter = MutableStateFlow(2)

    private fun getTemperatureLapTime() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.getTemperatureVsLapTimes()){
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.toString()
                            )
                        }
                    }
                    is Result.Success -> {
                        val sortedData = result.data.sortedBy { it.trackTemperature }
                        _state.update {
                            it.copy(temperatureLapTime = sortedData)
                        }
                        loadingCounter.update {
                            it-1
                        }
                        if(loadingCounter.value==0){
                            _state.update {
                                it.copy(isLoading = false)
                            }
                        }
                    }
                }

        }
    }

    private fun getDrivers(){
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.getDrivers()){
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.toString()
                        )
                    }
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
                        it.copy(
                            driversDifference = sortedPerformanceDifferences
                        )
                    }
                    loadingCounter.update {
                        it-1
                    }
                    if(loadingCounter.value==0){
                        _state.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }
}