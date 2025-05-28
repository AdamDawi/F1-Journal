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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.adamdawi.f1journal.presentation.components.ErrorDialog
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
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        var showError by remember { mutableStateOf(false) }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ){
            Column(
                modifier = Modifier.Companion.fillMaxSize(),
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navigator.push(
                            DetailsScreen(Random.Default.nextInt())
                        )
                    }
                ) {
                    Text("Go to next screen")
                }

                ErrorDialog(show = showError, message = "Error importing file", onDismiss = { showError = false })
            }
            Row(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            val file = chooseXmlFile()

                            if(file?.extension == "xml"){
                                viewModel.onAction(HomeAction.SendXMLFile(file))
                            }else{
                                showError = true
                            }
                        }
                    }
                ) {
                    Text("Import XML file")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            val file = chooseJsonFile()

                            if(file?.extension == "json"){
                                viewModel.onAction(HomeAction.SendJSONFile(file))
                            }else{
                                showError = true
                            }
                        }
                    }
                ) {
                    Text("Import JSON file")
                }
            }
        }
    }
}

fun chooseXmlFile(): File? {
    val fileDialog =
        FileDialog(null as Frame?, "Choose XML file", FileDialog.LOAD)
    fileDialog.file = "*.xml" // extension filter
    fileDialog.isVisible = true
    return if (fileDialog.file != null && fileDialog.file.endsWith(".xml")) {
        File(fileDialog.directory, fileDialog.file)
    } else {
        null
    }
}

fun chooseJsonFile(): File? {
    val fileDialog = FileDialog(null as Frame?, "Choose JSON file", FileDialog.LOAD)
    fileDialog.file = "*.json"
    fileDialog.isVisible = true
    return if (fileDialog.file != null && fileDialog.file.endsWith(".json", ignoreCase = true)) {
        File(fileDialog.directory, fileDialog.file)
    } else {
        null
    }
}



