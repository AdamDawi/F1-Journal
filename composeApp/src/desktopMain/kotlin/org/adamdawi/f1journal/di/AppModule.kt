package org.adamdawi.f1journal.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.adamdawi.f1journal.data.networking.HttpClientFactory
import org.adamdawi.f1journal.data.repository.F1RepositoryImpl
import org.adamdawi.f1journal.domain.F1Repository
import org.adamdawi.f1journal.presentation.home_screen.HomeViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeViewModel)
    single<HttpClient> { HttpClientFactory.build(CIO.create()) }

    singleOf(::F1RepositoryImpl) { bind<F1Repository>() }
}