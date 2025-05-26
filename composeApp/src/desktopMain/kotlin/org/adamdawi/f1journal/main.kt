package org.adamdawi.f1journal

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.adamdawi.f1journal.di.appModule
import org.adamdawi.f1journal.presentation.App
import org.koin.core.context.GlobalContext.startKoin

fun main() = application {
    startKoin {
        modules(appModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "F1 Journal"
    ) {
        App()
    }
}