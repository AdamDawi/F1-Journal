package org.adamdawi.f1journal.di

import org.adamdawi.f1journal.presentation.home_screen.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeViewModel)
}