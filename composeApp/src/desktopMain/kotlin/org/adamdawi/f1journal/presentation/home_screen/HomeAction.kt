package org.adamdawi.f1journal.presentation.home_screen

import java.io.File

sealed interface HomeAction {
    data class SendXMLFile(val file: File) : HomeAction
    data class SendJSONFile(val file: File) : HomeAction
}