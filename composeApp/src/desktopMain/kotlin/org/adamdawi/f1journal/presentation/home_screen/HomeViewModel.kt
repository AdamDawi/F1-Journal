package org.adamdawi.f1journal.presentation.home_screen

import androidx.lifecycle.ViewModel
import org.json.JSONObject
import org.json.XML
import java.io.File

class HomeViewModel: ViewModel() {

    fun onAction(action: HomeAction){
        when(action){
            is HomeAction.SendXMLFile -> {
                val jsonObject = convertXmlFileToJson(action.file)
                println(jsonObject)
            }
        }
    }

    private fun convertXmlFileToJson(file: File): JSONObject {
        println(file.readText())
        val xmlContent = file.readText()
        return XML.toJSONObject(xmlContent)
    }
}