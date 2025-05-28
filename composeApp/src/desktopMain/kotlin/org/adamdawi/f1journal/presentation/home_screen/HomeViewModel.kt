package org.adamdawi.f1journal.presentation.home_screen

import androidx.lifecycle.ViewModel
import org.adamdawi.f1journal.domain.F1Repository
import org.json.JSONObject
import org.json.XML
import java.io.File

class HomeViewModel(
    private val repository: F1Repository
): ViewModel() {

    fun onAction(action: HomeAction){
        when(action){
            is HomeAction.SendXMLFile -> {
                val jsonObject = convertXmlFileToJson(action.file)
                println(jsonObject)
            }

            is HomeAction.SendJSONFile -> {
                val jsonObject = action.file.readText()
                println(jsonObject)
            }
        }
    }

    private fun convertXmlFileToJson(file: File): JSONObject {
        val xmlContent = file.readText()
        return XML.toJSONObject(xmlContent)
    }
}