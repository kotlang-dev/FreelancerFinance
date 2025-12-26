package org.kotlang.freelancerfinance.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.kotlang.freelancerfinance.data.local.AppDatabase
import org.kotlang.freelancerfinance.data.local.dao.ClientDao
import org.kotlang.freelancerfinance.data.local.dao.InvoiceDao
import org.kotlang.freelancerfinance.data.local.dao.ServiceItemDao
import org.kotlang.freelancerfinance.data.preferences.PreferenceRepositoryImpl
import org.kotlang.freelancerfinance.data.repository.ProfileRepositoryImpl
import org.kotlang.freelancerfinance.data.preferences.PreferenceRepository
import org.kotlang.freelancerfinance.data.repository.ClientRepositoryImpl
import org.kotlang.freelancerfinance.data.repository.InvoiceRepositoryImpl
import org.kotlang.freelancerfinance.data.repository.ServiceItemRepositoryImpl
import org.kotlang.freelancerfinance.domain.repository.ClientRepository
import org.kotlang.freelancerfinance.domain.repository.InvoiceRepository
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository
import org.kotlang.freelancerfinance.domain.repository.ServiceItemRepository
import org.kotlang.freelancerfinance.presentation.add_edit_client.AddEditClientViewModel
import org.kotlang.freelancerfinance.presentation.add_edit_service.AddEditServiceViewModel
import org.kotlang.freelancerfinance.presentation.manage_client.ManageClientViewModel
import org.kotlang.freelancerfinance.presentation.dashboard.DashboardViewModel
import org.kotlang.freelancerfinance.presentation.create_invoice.CreateInvoiceViewModel
import org.kotlang.freelancerfinance.presentation.manage_services.ManageServicesViewModel
import org.kotlang.freelancerfinance.presentation.preview_invoice.PreviewInvoiceViewModel
import org.kotlang.freelancerfinance.presentation.profile.ProfileViewModel

val sharedModule = module {

    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single<ClientDao> { get<AppDatabase>().clientDao }
    single<InvoiceDao> { get<AppDatabase>().invoiceDao }
    single<ServiceItemDao> { get<AppDatabase>().serviceItemDao }

    singleOf(::PreferenceRepositoryImpl) bind PreferenceRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    singleOf(::ClientRepositoryImpl) bind ClientRepository::class
    singleOf(::InvoiceRepositoryImpl) bind InvoiceRepository::class
    singleOf(::ServiceItemRepositoryImpl) bind ServiceItemRepository::class

    viewModelOf(::ProfileViewModel)
    viewModelOf(::ManageClientViewModel)
    viewModelOf(::CreateInvoiceViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::AddEditClientViewModel)
    viewModelOf(::ManageServicesViewModel)
    viewModelOf(::AddEditServiceViewModel)
    viewModelOf(::PreviewInvoiceViewModel)

}