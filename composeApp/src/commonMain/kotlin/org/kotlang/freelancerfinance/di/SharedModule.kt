package org.kotlang.freelancerfinance.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.kotlang.freelancerfinance.data.preferences.PreferenceRepositoryImpl
import org.kotlang.freelancerfinance.data.repository.ProfileRepositoryImpl
import org.kotlang.freelancerfinance.data.preferences.PreferenceRepository
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository
import org.kotlang.freelancerfinance.presentation.ProfileViewModel

val sharedModule = module {

    singleOf(::PreferenceRepositoryImpl) bind PreferenceRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class

    viewModelOf(::ProfileViewModel)
}