package org.adamdawi.f1journal.presentation.home_screen

interface HomeEvent {
    data class Error(val error: String) : HomeEvent
    data class Info(val info: String) : HomeEvent
}
