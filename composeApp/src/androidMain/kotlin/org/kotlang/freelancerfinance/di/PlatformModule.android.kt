package org.kotlang.freelancerfinance.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.kotlang.freelancerfinance.data.local.AppDatabase
import org.kotlang.freelancerfinance.data.repository.AndroidPdfGenerator
import org.kotlang.freelancerfinance.data.util.Constants
import org.kotlang.freelancerfinance.domain.repository.PdfGenerator

actual val platformModule = module {

    single<RoomDatabase.Builder<AppDatabase>> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = Constants.DATABASE_NAME
        )
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile(Constants.DATASTORE_NAME)
        }
    }

    single<PdfGenerator> { AndroidPdfGenerator(androidContext()) }

}