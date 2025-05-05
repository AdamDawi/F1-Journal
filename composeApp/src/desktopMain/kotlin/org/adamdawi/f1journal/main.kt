package org.adamdawi.f1journal

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "F1-Journal",
    ) {
        App()
    }
}