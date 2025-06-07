package org.adamdawi.f1journal.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.adamdawi.f1journal.domain.repository.F1Repository
import org.adamdawi.f1journal.domain.util.Result
import org.json.JSONObject
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

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SendXMLFile -> {
                val jsonObject = convertXmlFileToJson(action.file)
                println(jsonObject)
            }

            is HomeAction.SendJSONFile -> {
                val jsonObject = action.file.readText()
                println(jsonObject)
            }
            else -> {}
        }
    }

    private fun convertXmlFileToJson(file: File): JSONObject {
        val xmlContent = file.readText()
        return XML.toJSONObject(xmlContent)
    }

    suspend fun getExportedJson(): String =
        when (val result = repository.getF1Data()) {
            is Result.Error -> {
                result.error.name
            }

            is Result.Success -> {
                Json.encodeToString(result.data)
            }
        }


    suspend fun getExportedXML(): String =
        when (val result = repository.getF1Data()) {
            is Result.Error -> {
                result.error.name
            }

            is Result.Success -> {
                val xmlMapper = XmlMapper()
                val xmlString = xmlMapper.writeValueAsString(result.data)

                xmlString
            }
        }
}