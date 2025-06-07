package org.adamdawi.f1journal.presentation.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.adamdawi.f1journal.presentation.components.ErrorDialog
import org.adamdawi.f1journal.presentation.components.LoadingScreen
import org.adamdawi.f1journal.presentation.details_screen.DetailsScreen
import org.koin.compose.viewmodel.koinViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import kotlin.random.Random

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = koinViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow

        if(state.value.isLoading){
            LoadingScreen()
        }else{
            HomeContent(
                scope = scope,
                onAction = { action ->
                    when (action) {
                        HomeAction.OnNavigateToCharts -> {
                            navigator.push(
                                DetailsScreen(Random.nextInt())
                            )
                        }

                        else -> viewModel.onAction(action)
                    }
                },
                getExportedJson = {
                    viewModel.getExportedJson()
                },
                getExportedXML = {
                    viewModel.getExportedXML()
                }
            )
        }

    }
}

@Composable
fun HomeContent(
    scope: CoroutineScope,
    onAction: (HomeAction) -> Unit,
    getExportedJson: suspend () -> String,
    getExportedXML: suspend () -> String
){
    var showError by remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    onAction(HomeAction.OnNavigateToCharts)
                }
            ) {
                Text("Go to charts screen")
            }

            ErrorDialog(show = showError, message = "Error importing/exporting file", onDismiss = { showError = false })
        }
        Row(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        val file = chooseFile(filter = ExtensionFilter.XML)

                        if(file?.extension == "xml"){
                            onAction(HomeAction.SendXMLFile(file))
                        }else{
                            showError = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text("Import XML file")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        val file = chooseFile(ExtensionFilter.JSON)

                        if(file?.extension == "json"){
                            onAction(HomeAction.SendJSONFile(file))
                        }else{
                            showError = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text("Import JSON file")
            }
        }

        Row(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        val file = chooseFile(filter = ExtensionFilter.XML, isSave = true)

                        if(file != null && file.extension == "xml"){
                            val xmlData = getExportedXML()
                            file.writeText(xmlData)
                        } else {
                            showError = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text("Export XML file")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        val file = chooseFile(filter = ExtensionFilter.JSON, isSave = true)

                        if(file != null && file.extension == "json"){
                            val jsonData = getExportedJson()
                            file.writeText(jsonData)
                        } else {
                            showError = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text("Export JSON file")
            }
        }
    }
}

private fun chooseFile(filter: ExtensionFilter, isSave: Boolean = false): File? {
    val fileDialog = FileDialog(null as Frame?, if (isSave) "Save File" else "Choose File",
        if (isSave) FileDialog.SAVE else FileDialog.LOAD)

    fileDialog.file = filter.extension
    fileDialog.isVisible = true

    return if (fileDialog.file != null) {
        File(fileDialog.directory, fileDialog.file)
    } else {
        null
    }
}

private enum class ExtensionFilter(val extension: String){
    JSON("*.json"),
    XML("*.xml")
}