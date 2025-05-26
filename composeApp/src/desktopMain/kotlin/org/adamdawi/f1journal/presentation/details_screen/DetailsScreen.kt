package org.adamdawi.f1journal.presentation.details_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.adamdawi.f1journal.data.api.ApiService
import org.json.JSONObject
import org.json.XML
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import org.adamdawi.f1journal.BuildConfig


data class DetailsScreen(val id: Int): Screen {
    @Composable
    override fun Content() {
        var apiResponse by remember { mutableStateOf("Waiting...") }
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier.Companion.fillMaxSize(),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navigator.pop()
                }
            ) {
                Text(id.toString())
            }
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
//                        val result = ApiService().fetchPosts()
//                        apiResponse = result
                        val file = chooseXmlFile()
                        println(file?.readText())
                        file?.let {
                            println(convertXmlFileToJson(file).toString())
                        }
                    }
                }
            ) {
                Text("Make request")
            }
            Text(apiResponse)
        }
    }
}

fun chooseXmlFile(): File? {
    val fileDialog = FileDialog(null as Frame?, "Choose XML file", FileDialog.LOAD)
    fileDialog.file = "*.xml" // extension filter
    fileDialog.isVisible = true
    return if (fileDialog.file != null && fileDialog.file.endsWith(".xml")) {
        File(fileDialog.directory, fileDialog.file)
    } else {
        null
    }
}

fun convertXmlFileToJson(file: File): JSONObject {
    val xmlContent = file.readText()
    return XML.toJSONObject(xmlContent)
}
