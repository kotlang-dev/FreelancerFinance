package org.kotlang.freelancerfinance.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.kotlang.freelancerfinance.data.local.AppDatabase
import org.kotlang.freelancerfinance.data.local.ClientDao
import org.kotlang.freelancerfinance.data.preferences.PreferenceRepositoryImpl
import org.kotlang.freelancerfinance.data.repository.ProfileRepositoryImpl
import org.kotlang.freelancerfinance.data.preferences.PreferenceRepository
import org.kotlang.freelancerfinance.data.repository.ClientRepositoryImpl
import org.kotlang.freelancerfinance.domain.repository.ClientRepository
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository
import org.kotlang.freelancerfinance.presentation.client_list.ClientListViewModel
import org.kotlang.freelancerfinance.presentation.profile.ProfileViewModel

val sharedModule = module {

    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single<ClientDao> { get<AppDatabase>().clientDao }

    singleOf(::PreferenceRepositoryImpl) bind PreferenceRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    singleOf(::ClientRepositoryImpl) bind ClientRepository::class

    viewModelOf(::ProfileViewModel)
    viewModelOf(::ClientListViewModel)
}