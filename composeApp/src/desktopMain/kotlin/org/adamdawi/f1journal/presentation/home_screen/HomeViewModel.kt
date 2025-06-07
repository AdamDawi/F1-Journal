package org.adamdawi.f1journal.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.adamdawi.f1journal.domain.model.DataType
import org.adamdawi.f1journal.domain.repository.F1Repository
import org.adamdawi.f1journal.domain.util.Result
import org.json.JSONArray
import org.json.XML
import java.io.File

class HomeViewModel(
    private val repository: F1Repository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.onStart {

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SendXMLFile -> {
                sendDataToServer(action.file, DataType.XML)
            }

            is HomeAction.SendJSONFile -> {
                sendDataToServer(action.file, DataType.JSON)
            }
            else -> {}
        }
    }

    private fun sendDataToServer(file: File, dataType: DataType){
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val jsonObject: String = when (dataType) {
                DataType.JSON -> file.readText()
                DataType.XML -> convertXmlFileToJson(file).toString()
            }
            println(jsonObject)
            when(val result = repository.sendF1Data(jsonObject)){
                is Result.Error -> {
                    withContext(Dispatchers.Main) {
                        _state.update {
                            it.copy(isLoading = false)
                        }
                        eventChannel.send(HomeEvent.Error(result.error.name))
                    }
                }
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        _state.update {
                            it.copy(isLoading = false)
                        }
                        eventChannel.send(HomeEvent.Info("Import Success"))
                    }
                }
            }
            withContext(Dispatchers.Main){
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    private fun convertXmlFileToJson(file: File): JSONArray {
        val xmlContent = file.readText()
        val jsonObject = XML.toJSONObject(xmlContent)

        // Get the "drivers" array directly
        val drivers = jsonObject.optJSONObject("drivers")?.optJSONArray("driver") ?: return JSONArray(jsonObject)

        // Iterate through each driver and adjust their structure
        for (i in 0 until drivers.length()) {
            val driver = drivers.getJSONObject(i)

            // Adjusting the lapTimeList and raceList to be empty arrays, not empty strings
            driver.put("lapTimeList", JSONArray()) // Convert lapTimeList to an empty array
            driver.put("raceList", JSONArray()) // Convert raceList to an empty array

            // Adjust resultList to be an array of result objects, not nested under a "result" key
            val resultList = driver.optJSONObject("resultList")
            resultList?.let {
                val result = it.optJSONObject("result")
                if (result != null) {
                    // Replace the "resultList" with an array containing a single result object
                    driver.put("resultList", JSONArray().put(result))
                }
            }
        }

        // Return the drivers array directly without the "drivers" wrapper
        return drivers
    }

    suspend fun getExportedData(dataType: DataType): String {
        withContext(Dispatchers.Main) {
            _state.update {
                it.copy(isLoading = true)
            }
        }
        when (val result = repository.getF1Data()) {
            is Result.Error -> {
                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    eventChannel.send(HomeEvent.Error(result.error.name))
                }
                return result.error.name
            }

            is Result.Success -> {
                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    eventChannel.send(HomeEvent.Info("Export Success"))
                }
                when(dataType){
                    DataType.JSON -> {
                        return Json.encodeToString(result.data)
                    }
                    DataType.XML -> {
                        val xmlMapper = XmlMapper()
                        val xmlString = xmlMapper.writeValueAsString(result.data)

                        return xmlString
                    }
                }
            }
        }
    }
}