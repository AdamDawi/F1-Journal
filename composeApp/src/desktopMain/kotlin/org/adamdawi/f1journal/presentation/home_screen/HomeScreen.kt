package org.adamdawi.f1journal.presentation.home_screen

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
            ErrorDialog(show = showError, message = "Error importing file", onDismiss = { showError = false })
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



