package org.kotlang.freelancerfinance.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.RoomDatabase
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import org.kotlang.freelancerfinance.data.local.AppDatabase
import org.kotlang.freelancerfinance.data.local.getDesktopFile
import org.kotlang.freelancerfinance.data.repository.DesktopFileOpener
import org.kotlang.freelancerfinance.data.repository.DesktopPdfGenerator
import org.kotlang.freelancerfinance.data.util.Constants
import org.kotlang.freelancerfinance.domain.repository.FileOpener
import org.kotlang.freelancerfinance.domain.repository.PdfGenerator

actual val platformModule: Module = module {

    single<RoomDatabase.Builder<AppDatabase>> {
        val dbFile = getDesktopFile(fileName = Constants.DATABASE_NAME)
        Room.databaseBuilder<AppDatabase>(dbFile.absolutePath)
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            getDesktopFile(Constants.DATASTORE_NAME).absolutePath.toPath()
        }
    }

    single<PdfGenerator> { DesktopPdfGenerator() }
    single<FileOpener> { DesktopFileOpener() }
}